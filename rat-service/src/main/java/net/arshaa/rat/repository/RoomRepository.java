package net.arshaa.rat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Models.RoomDto;
import net.arshaa.rat.entity.Floors;
import net.arshaa.rat.entity.Rooms;

public interface RoomRepository extends JpaRepository<Rooms, Integer>{

	public Optional<List<Rooms>> findByFloorId(Integer id);

	public boolean existsByRoomNumber(int roomId);

	public boolean existsByFloorId(int floorId);

	public Rooms getRoomNumberByRoomId(int roomId);

	public List<Rooms> getRoomsByFloorId(int floorId);

	Rooms getRoomNameByRoomId(int roomId) ;

	


}
