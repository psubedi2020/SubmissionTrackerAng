import dayjs from 'dayjs/esm';

import { SubmissionStatus } from 'app/entities/enumerations/submission-status.model';

import { ISubmission, NewSubmission } from './submission.model';

export const sampleWithRequiredData: ISubmission = {
  id: 80990,
  companyName: 'SQL',
};

export const sampleWithPartialData: ISubmission = {
  id: 34036,
  jobTitle: 'Customer Identity Designer',
  companyName: 'Director mint Montana',
  submissionStatus: SubmissionStatus['SUBMITTED'],
};

export const sampleWithFullData: ISubmission = {
  id: 44566,
  jobTitle: 'Customer Operations Architect',
  companyName: 'synthesize Profit-focused program',
  jobURL: 'visionary',
  quotedSalary: 281526,
  jobRequisitionId: 'Toys Representative generate',
  applicationDate: dayjs('2023-01-22T18:25'),
  submissionStatus: SubmissionStatus['DECIDED'],
};

export const sampleWithNewData: NewSubmission = {
  companyName: 'Intelligent overriding',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
