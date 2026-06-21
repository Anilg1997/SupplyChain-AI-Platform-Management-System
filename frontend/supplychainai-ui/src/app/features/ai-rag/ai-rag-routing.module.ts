import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AiRagComponent } from './ai-rag.component';

const routes: Routes = [{ path: '', component: AiRagComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AiRagRoutingModule { }
