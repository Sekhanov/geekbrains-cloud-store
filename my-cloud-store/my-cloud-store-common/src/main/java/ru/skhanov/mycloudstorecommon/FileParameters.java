package ru.skhanov.mycloudstorecommon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileParameters {

	private String name;
	private int size;
	private String date;
}
