import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { ContractsRoutingModule } from './contracts-routing.module';
import { ContractsComponent } from './contracts.component';


@NgModule({
  declarations: [
    ContractsComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ContractsRoutingModule
  ]
})
export class ContractsModule { }
