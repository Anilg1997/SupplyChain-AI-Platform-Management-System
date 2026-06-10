import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { PoRoutingModule } from './po-routing.module';
import { PoComponent } from './po.component';


@NgModule({
  declarations: [
    PoComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    PoRoutingModule
  ]
})
export class PoModule { }
