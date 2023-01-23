import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICandidate, NewCandidate } from '../candidate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICandidate for edit and NewCandidateFormGroupInput for create.
 */
type CandidateFormGroupInput = ICandidate | PartialWithRequiredKeyOf<NewCandidate>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICandidate | NewCandidate> = Omit<T, 'availableDate'> & {
  availableDate?: string | null;
};

type CandidateFormRawValue = FormValueOf<ICandidate>;

type NewCandidateFormRawValue = FormValueOf<NewCandidate>;

type CandidateFormDefaults = Pick<NewCandidate, 'id' | 'availableDate'>;

type CandidateFormGroupContent = {
  id: FormControl<CandidateFormRawValue['id'] | NewCandidate['id']>;
  firstName: FormControl<CandidateFormRawValue['firstName']>;
  lastName: FormControl<CandidateFormRawValue['lastName']>;
  email: FormControl<CandidateFormRawValue['email']>;
  phoneNumber: FormControl<CandidateFormRawValue['phoneNumber']>;
  availableDate: FormControl<CandidateFormRawValue['availableDate']>;
  expectedSalary: FormControl<CandidateFormRawValue['expectedSalary']>;
};

export type CandidateFormGroup = FormGroup<CandidateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CandidateFormService {
  createCandidateFormGroup(candidate: CandidateFormGroupInput = { id: null }): CandidateFormGroup {
    const candidateRawValue = this.convertCandidateToCandidateRawValue({
      ...this.getFormDefaults(),
      ...candidate,
    });
    return new FormGroup<CandidateFormGroupContent>({
      id: new FormControl(
        { value: candidateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(candidateRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(candidateRawValue.lastName, {
        validators: [Validators.required],
      }),
      email: new FormControl(candidateRawValue.email),
      phoneNumber: new FormControl(candidateRawValue.phoneNumber, {
        validators: [Validators.pattern('^(\\([0-9]{3}\\)|[0-9]{3}-)[0-9]{3}-[0-9]{4}$')],
      }),
      availableDate: new FormControl(candidateRawValue.availableDate),
      expectedSalary: new FormControl(candidateRawValue.expectedSalary),
    });
  }

  getCandidate(form: CandidateFormGroup): ICandidate | NewCandidate {
    return this.convertCandidateRawValueToCandidate(form.getRawValue() as CandidateFormRawValue | NewCandidateFormRawValue);
  }

  resetForm(form: CandidateFormGroup, candidate: CandidateFormGroupInput): void {
    const candidateRawValue = this.convertCandidateToCandidateRawValue({ ...this.getFormDefaults(), ...candidate });
    form.reset(
      {
        ...candidateRawValue,
        id: { value: candidateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CandidateFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      availableDate: currentTime,
    };
  }

  private convertCandidateRawValueToCandidate(rawCandidate: CandidateFormRawValue | NewCandidateFormRawValue): ICandidate | NewCandidate {
    return {
      ...rawCandidate,
      availableDate: dayjs(rawCandidate.availableDate, DATE_TIME_FORMAT),
    };
  }

  private convertCandidateToCandidateRawValue(
    candidate: ICandidate | (Partial<NewCandidate> & CandidateFormDefaults)
  ): CandidateFormRawValue | PartialWithRequiredKeyOf<NewCandidateFormRawValue> {
    return {
      ...candidate,
      availableDate: candidate.availableDate ? candidate.availableDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
