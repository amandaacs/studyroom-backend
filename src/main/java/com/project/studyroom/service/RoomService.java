package com.project.studyroom.service;

import com.project.studyroom.dto.RoomStatusDTO;
import com.project.studyroom.model.Reservation;
import com.project.studyroom.model.Room;
import com.project.studyroom.repository.ReservationRepository;
import com.project.studyroom.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public String createRoom(Room room) throws Exception {
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() throws Exception {
        return roomRepository.findAll();
    }

    public List<Room> getAvailableRooms(Date start, Date end) throws Exception {
        if(start.after(end)) {
            throw new IllegalArgumentException("A data de início deve ser antes da data de fim.");
        }

        List<Room> allRooms = roomRepository.findAll();
        List<Room> availableRooms = new ArrayList<>();

        for(Room room : allRooms) {
            List<Reservation> conflitos = reservationRepository.findOverlapping(room.getId(), start, end);

            if(conflitos.isEmpty()) {
                availableRooms.add(room);
            }
        }

        return availableRooms;

    }

    public List<RoomStatusDTO> getCurrentRoomStatus() throws Exception {
        List<Room> allRooms = roomRepository.findAll();
        Date now = new Date();
        List<RoomStatusDTO> statusList = new ArrayList<>();

        for (Room room : allRooms) {
            List<Reservation> active = reservationRepository.findOverlapping(
                    room.getId(), now, new Date(now.getTime() + 1000)
            );

            RoomStatusDTO dto = new RoomStatusDTO();
            dto.setRoomId(room.getId());
            dto.setRoomName(room.getName());

            if (!active.isEmpty()) {
                dto.setOccupied(true);
                dto.setCurrentOccupant(active.get(0).getUserName());
                dto.setUntil(active.get(0).getEndTime().toString());
            } else {
                dto.setOccupied(false);
            }
            statusList.add(dto);

        }
        return statusList;
    }
}
