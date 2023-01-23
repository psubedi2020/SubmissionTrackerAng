import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SubmissionFormService, SubmissionFormGroup } from './submission-form.service';
import { ISubmission } from '../submission.model';
import { SubmissionService } from '../service/submission.service';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { SubmissionStatus } from 'app/entities/enumerations/submission-status.model';

@Component({
  selector: 'jhi-submission-update',
  templateUrl: './submission-update.component.html',
})
export class SubmissionUpdateComponent implements OnInit {
  isSaving = false;
  submission: ISubmission | null = null;
  submissionStatusValues = Object.keys(SubmissionStatus);

  candidatesSharedCollection: ICandidate[] = [];

  editForm: SubmissionFormGroup = this.submissionFormService.createSubmissionFormGroup();

  constructor(
    protected submissionService: SubmissionService,
    protected submissionFormService: SubmissionFormService,
    protected candidateService: CandidateService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCandidate = (o1: ICandidate | null, o2: ICandidate | null): boolean => this.candidateService.compareCandidate(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ submission }) => {
      this.submission = submission;
      if (submission) {
        this.updateForm(submission);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const submission = this.submissionFormService.getSubmission(this.editForm);
    if (submission.id !== null) {
      this.subscribeToSaveResponse(this.submissionService.update(submission));
    } else {
      this.subscribeToSaveResponse(this.submissionService.create(submission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubmission>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(submission: ISubmission): void {
    this.submission = submission;
    this.submissionFormService.resetForm(this.editForm, submission);

    this.candidatesSharedCollection = this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(
      this.candidatesSharedCollection,
      submission.candidate
    );
  }

  protected loadRelationshipsOptions(): void {
    this.candidateService
      .query()
      .pipe(map((res: HttpResponse<ICandidate[]>) => res.body ?? []))
      .pipe(
        map((candidates: ICandidate[]) =>
          this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(candidates, this.submission?.candidate)
        )
      )
      .subscribe((candidates: ICandidate[]) => (this.candidatesSharedCollection = candidates));
  }
}
