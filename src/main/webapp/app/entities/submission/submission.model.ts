import dayjs from 'dayjs/esm';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { SubmissionStatus } from 'app/entities/enumerations/submission-status.model';

export interface ISubmission {
  id: number;
  jobTitle?: string | null;
  companyName?: string | null;
  jobURL?: string | null;
  quotedSalary?: number | null;
  jobRequisitionId?: string | null;
  applicationDate?: dayjs.Dayjs | null;
  submissionStatus?: SubmissionStatus | null;
  candidate?: Pick<ICandidate, 'id'> | null;
}

export type NewSubmission = Omit<ISubmission, 'id'> & { id: null };
