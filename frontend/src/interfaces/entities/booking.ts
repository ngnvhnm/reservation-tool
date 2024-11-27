export default interface Booking {
  id: string;
  startTime: Date;
  endTime: Date;
  timeOfBooking: Date;
  conferenceType: string;
  bookerEmail: string;
  attendeeList: string;
}
