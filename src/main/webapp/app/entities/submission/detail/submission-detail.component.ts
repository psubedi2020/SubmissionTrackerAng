import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubmission } from '../submission.model';

@Component({
  selector: 'jhi-submission-detail',
  templateUrl: './submission-detail.component.html',
})
export class SubmissionDetailComponent implements OnInit {
  submission: ISubmission | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ submission }) => {
      this.submission = submission;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
