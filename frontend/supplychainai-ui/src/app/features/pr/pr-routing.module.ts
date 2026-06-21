import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PrComponent } from './pr.component';

const routes: Routes = [{ path: '', component: PrComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PrRoutingModule { }
