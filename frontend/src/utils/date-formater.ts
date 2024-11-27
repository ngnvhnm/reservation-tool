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

export { getDateOnly, getHourAndMinutes };
