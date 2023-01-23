import dayjs from 'dayjs/esm';

import { ICandidate, NewCandidate } from './candidate.model';

export const sampleWithRequiredData: ICandidate = {
  id: 59103,
  firstName: 'Eusebio',
  lastName: 'Wolff',
};

export const sampleWithPartialData: ICandidate = {
  id: 59005,
  firstName: 'Theodora',
  lastName: 'Koss',
  email: 'Solon_Jacobi@yahoo.com',
  phoneNumber: '740-083-3207',
  availableDate: dayjs('2023-01-22T12:13'),
};

export const sampleWithFullData: ICandidate = {
  id: 1286,
  firstName: 'Minnie',
  lastName: 'Wunsch',
  email: 'Amely.Welch97@gmail.com',
  phoneNumber: '(729)919-1838',
  availableDate: dayjs('2023-01-22T15:46'),
  expectedSalary: 38681,
};

export const sampleWithNewData: NewCandidate = {
  firstName: 'Kallie',
  lastName: 'Dickinson',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
