import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { PrRoutingModule } from './pr-routing.module';
import { PrComponent } from './pr.component';


@NgModule({
  declarations: [
    PrComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    PrRoutingModule
  ]
})
export class PrModule { }
