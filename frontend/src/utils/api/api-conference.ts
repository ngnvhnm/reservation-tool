import loadEnv from '../load-env.ts';
import { headers, Message } from './index.ts';
import Booking from '../../interfaces/entities/booking.ts';

const route = `${loadEnv().SPRING_BACKEND_ADDRESS}/v1/conferences`;

// type CONF_TYPE = 'CONFERENCE_OG2' | 'CONFERENCE_OG3';

export interface CreateBookingTypeConferenceDto {
  startTime: Date;
  endTime: Date;
  bookerEmail: string;
  conferenceType: string; // TODO: change to CONF_TYPE
  attendeeList: string; // Comma separated list of emails
}

export const createBookingTypeConference = async (body: CreateBookingTypeConferenceDto) => {
  const response = await fetch(`${route}/create-event`, {
    method: 'POST',
    headers: { ...(await headers()) },
    body: JSON.stringify({ ...body }),
  });

  const data = (await response.json()) as unknown;
  if (!response.ok) {
    throw new Error((data as Message).message);
  }
};

export const getAllBookingTypeConferenceByUser = async (email: string) => {
  const response = await fetch(`${route}?mail=${email}`, {
    headers: { ...(await headers()) },
  });

  const data = (await response.json()) as unknown;
  if (!response.ok) {
    throw new Error((data as Message).message);
  }
  return data as Booking[];
};

export const deleteBookingTypeConference = async (id: string) => {
  const response = await fetch(`${route}/${id}`, {
    method: 'DELETE',
    headers: { ...(await headers()) },
  });

  const data = (await response.json()) as unknown;
  if (!response.ok) {
    throw new Error((data as Message).message);
  }
};

export const editBookingTypeConference = async (
  id: string,
  body: CreateBookingTypeConferenceDto,
) => {
  const response = await fetch(`${route}/${id}`, {
    method: 'PUT',
    headers: { ...(await headers()) },
    body: JSON.stringify({ ...body }),
  });

  const data = (await response.json()) as unknown;
  if (!response.ok) {
    throw new Error((data as Message).message);
  }
};
