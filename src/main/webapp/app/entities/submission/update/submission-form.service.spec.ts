import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../submission.test-samples';

import { SubmissionFormService } from './submission-form.service';

describe('Submission Form Service', () => {
  let service: SubmissionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubmissionFormService);
  });

  describe('Service methods', () => {
    describe('createSubmissionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubmissionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            jobTitle: expect.any(Object),
            companyName: expect.any(Object),
            jobURL: expect.any(Object),
            quotedSalary: expect.any(Object),
            jobRequisitionId: expect.any(Object),
            applicationDate: expect.any(Object),
            submissionStatus: expect.any(Object),
            candidate: expect.any(Object),
          })
        );
      });

      it('passing ISubmission should create a new form with FormGroup', () => {
        const formGroup = service.createSubmissionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            jobTitle: expect.any(Object),
            companyName: expect.any(Object),
            jobURL: expect.any(Object),
            quotedSalary: expect.any(Object),
            jobRequisitionId: expect.any(Object),
            applicationDate: expect.any(Object),
            submissionStatus: expect.any(Object),
            candidate: expect.any(Object),
          })
        );
      });
    });

    describe('getSubmission', () => {
      it('should return NewSubmission for default Submission initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSubmissionFormGroup(sampleWithNewData);

        const submission = service.getSubmission(formGroup) as any;

        expect(submission).toMatchObject(sampleWithNewData);
      });

      it('should return NewSubmission for empty Submission initial value', () => {
        const formGroup = service.createSubmissionFormGroup();

        const submission = service.getSubmission(formGroup) as any;

        expect(submission).toMatchObject({});
      });

      it('should return ISubmission', () => {
        const formGroup = service.createSubmissionFormGroup(sampleWithRequiredData);

        const submission = service.getSubmission(formGroup) as any;

        expect(submission).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISubmission should not enable id FormControl', () => {
        const formGroup = service.createSubmissionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSubmission should disable id FormControl', () => {
        const formGroup = service.createSubmissionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
