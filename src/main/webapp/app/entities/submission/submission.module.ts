import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SubmissionComponent } from './list/submission.component';
import { SubmissionDetailComponent } from './detail/submission-detail.component';
import { SubmissionUpdateComponent } from './update/submission-update.component';
import { SubmissionDeleteDialogComponent } from './delete/submission-delete-dialog.component';
import { SubmissionRoutingModule } from './route/submission-routing.module';

@NgModule({
  imports: [SharedModule, SubmissionRoutingModule],
  declarations: [SubmissionComponent, SubmissionDetailComponent, SubmissionUpdateComponent, SubmissionDeleteDialogComponent],
})
export class SubmissionModule {}
