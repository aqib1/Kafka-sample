package com.kafka.producer;

public class BookingEvents {
	private String id;
	private String hotel_Location;
	private String hotel_city;
	private String hotel_country;
	private boolean is_Booking;

	public BookingEvents() {
		super();
	}

	public BookingEvents(String id, String hotel_Location, String hotel_city, String hotel_country, boolean is_Booking) {
		super();
		this.id = id;
		this.hotel_Location = hotel_Location;
		this.hotel_city = hotel_city;
		this.hotel_country = hotel_country;
		this.is_Booking = is_Booking;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHotel_Location() {
		return hotel_Location;
	}

	public void setHotel_Location(String hotel_Location) {
		this.hotel_Location = hotel_Location;
	}

	public String getHotel_city() {
		return hotel_city;
	}

	public void setHotel_city(String hotel_city) {
		this.hotel_city = hotel_city;
	}

	public String getHotel_country() {
		return hotel_country;
	}

	public void setHotel_country(String hotel_country) {
		this.hotel_country = hotel_country;
	}

	public boolean isIs_Booking() {
		return is_Booking;
	}

	public void setIs_Booking(boolean is_Booking) {
		this.is_Booking = is_Booking;
	}

	@Override
	public String toString() {
		return "BookingEvents [id=" + id + ", hotel_Location=" + hotel_Location + ", hotel_city=" + hotel_city
				+ ", hotel_country=" + hotel_country + ", is_Booking=" + is_Booking + "]";
	}

}
