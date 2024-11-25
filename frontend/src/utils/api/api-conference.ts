import loadEnv from '../load-env.ts';
import { headers, Message } from './index.ts';

const route = `${loadEnv().SPRING_BACKEND_ADDRESS}/v1/conferences/create-event`;

// type CONF_TYPE = 'CONFERENCE_OG2' | 'CONFERENCE_OG3';

export interface CreateBookingTypeConferenceDto {
  startTime: Date;
  endTime: Date;
  bookerEmail: string;
  conferenceType: string; // TODO: change to CONF_TYPE
  attendeeList: string; // Comma separated list of emails
}
export const createBookingTypeConference = async (body: CreateBookingTypeConferenceDto) => {
  const response = await fetch(route, {
    method: 'POST',
    headers: { ...(await headers()) },
    body: JSON.stringify({ ...body }),
  });

  const data = (await response.json()) as unknown;
  if (!response.ok) {
    throw new Error((data as Message).message);
  }
};
