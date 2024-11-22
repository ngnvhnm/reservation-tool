
type Status = 'ongoing' | 'finished' | 'cancelled';

export default interface Booking {
  id: string;
  startDate: Date;
  endDate: Date;
  status: Status;
  bookingType: string;
}
