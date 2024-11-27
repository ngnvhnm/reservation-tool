/**
 * Format date to string (dd.mm.yyyy hh:mm)
 * @param date Date to format
 * @returns Formatted date
 */
const getDateOnly = (date: Date): string => {
  const day = date.getDate().toString().padStart(2, '0');
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const year = date.getFullYear();

  return `${day}.${month}.${year}`;
};

/**
 * Format date to string (hh:mm)
 * @param date Date to format
 * @returns Formatted date
 */
const getHourAndMinutes = (date: Date): string => {
  const hour = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');

  return `${hour}:${minutes}`;
};

/**
 * Get a full date from a string (dd.mm.yyyy hh:mm)
 * @param date Date string
 * @returns Date object
 */
const getFullDateGMT1 = (date: Date, time: string): Date => {
  const [hours, minutes] = time.split(':').map(Number); // Parse hours and minutes
  const newDate = new Date(date); // Create a copy of the date
  newDate.setHours(hours+1, minutes, 0, 0); // Set hours and minutes + 1
  return new Date(newDate.getTime());
};

export { getDateOnly, getHourAndMinutes, getFullDateGMT1 };
