import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISubmission, NewSubmission } from '../submission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubmission for edit and NewSubmissionFormGroupInput for create.
 */
type SubmissionFormGroupInput = ISubmission | PartialWithRequiredKeyOf<NewSubmission>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISubmission | NewSubmission> = Omit<T, 'applicationDate'> & {
  applicationDate?: string | null;
};

type SubmissionFormRawValue = FormValueOf<ISubmission>;

type NewSubmissionFormRawValue = FormValueOf<NewSubmission>;

type SubmissionFormDefaults = Pick<NewSubmission, 'id' | 'applicationDate'>;

type SubmissionFormGroupContent = {
  id: FormControl<SubmissionFormRawValue['id'] | NewSubmission['id']>;
  jobTitle: FormControl<SubmissionFormRawValue['jobTitle']>;
  companyName: FormControl<SubmissionFormRawValue['companyName']>;
  jobURL: FormControl<SubmissionFormRawValue['jobURL']>;
  quotedSalary: FormControl<SubmissionFormRawValue['quotedSalary']>;
  jobRequisitionId: FormControl<SubmissionFormRawValue['jobRequisitionId']>;
  applicationDate: FormControl<SubmissionFormRawValue['applicationDate']>;
  submissionStatus: FormControl<SubmissionFormRawValue['submissionStatus']>;
  candidate: FormControl<SubmissionFormRawValue['candidate']>;
};

export type SubmissionFormGroup = FormGroup<SubmissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubmissionFormService {
  createSubmissionFormGroup(submission: SubmissionFormGroupInput = { id: null }): SubmissionFormGroup {
    const submissionRawValue = this.convertSubmissionToSubmissionRawValue({
      ...this.getFormDefaults(),
      ...submission,
    });
    return new FormGroup<SubmissionFormGroupContent>({
      id: new FormControl(
        { value: submissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      jobTitle: new FormControl(submissionRawValue.jobTitle),
      companyName: new FormControl(submissionRawValue.companyName, {
        validators: [Validators.required],
      }),
      jobURL: new FormControl(submissionRawValue.jobURL),
      quotedSalary: new FormControl(submissionRawValue.quotedSalary, {
        validators: [Validators.min(10000), Validators.max(500000.0)],
      }),
      jobRequisitionId: new FormControl(submissionRawValue.jobRequisitionId),
      applicationDate: new FormControl(submissionRawValue.applicationDate),
      submissionStatus: new FormControl(submissionRawValue.submissionStatus),
      candidate: new FormControl(submissionRawValue.candidate),
    });
  }

  getSubmission(form: SubmissionFormGroup): ISubmission | NewSubmission {
    return this.convertSubmissionRawValueToSubmission(form.getRawValue() as SubmissionFormRawValue | NewSubmissionFormRawValue);
  }

  resetForm(form: SubmissionFormGroup, submission: SubmissionFormGroupInput): void {
    const submissionRawValue = this.convertSubmissionToSubmissionRawValue({ ...this.getFormDefaults(), ...submission });
    form.reset(
      {
        ...submissionRawValue,
        id: { value: submissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SubmissionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      applicationDate: currentTime,
    };
  }

  private convertSubmissionRawValueToSubmission(
    rawSubmission: SubmissionFormRawValue | NewSubmissionFormRawValue
  ): ISubmission | NewSubmission {
    return {
      ...rawSubmission,
      applicationDate: dayjs(rawSubmission.applicationDate, DATE_TIME_FORMAT),
    };
  }

  private convertSubmissionToSubmissionRawValue(
    submission: ISubmission | (Partial<NewSubmission> & SubmissionFormDefaults)
  ): SubmissionFormRawValue | PartialWithRequiredKeyOf<NewSubmissionFormRawValue> {
    return {
      ...submission,
      applicationDate: submission.applicationDate ? submission.applicationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
