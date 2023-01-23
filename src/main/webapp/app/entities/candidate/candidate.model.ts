import dayjs from 'dayjs/esm';

export interface ICandidate {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  availableDate?: dayjs.Dayjs | null;
  expectedSalary?: number | null;
}

export type NewCandidate = Omit<ICandidate, 'id'> & { id: null };
