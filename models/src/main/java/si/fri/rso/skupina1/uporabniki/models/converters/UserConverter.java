package si.fri.rso.skupina1.uporabniki.models.converters;

import si.fri.rso.skupina1.uporabniki.lib.User;
import si.fri.rso.skupina1.uporabniki.models.entities.UserEntity;

public class UserConverter {

	public static User toDto(UserEntity entity) {

		User dto = new User();
		dto.setUserId(entity.getId());
		dto.setName(entity.getName());
		dto.setSurname(entity.getSurname());
		dto.setCity(entity.getCity());
		dto.setPostalCode(entity.getPostalCode());
		dto.setAddress(entity.getAddress());
		dto.setCanDeliver(entity.getCanDeliver());

		return dto;

	}

	public static UserEntity toEntity(User dto) {

		UserEntity entity = new UserEntity();
		entity.setId(dto.getUserId());
		entity.setName(dto.getName());
		entity.setSurname(dto.getSurname());
		entity.setCity(dto.getCity());
		entity.setPostalCode(dto.getPostalCode());
		entity.setAddress(dto.getAddress());
		entity.setCanDeliver(dto.getCanDeliver());
		return entity;

	}

}
