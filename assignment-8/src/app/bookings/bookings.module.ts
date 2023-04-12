import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyBookingsComponent } from './my-bookings/my-bookings.component';
import { BookingsComponent } from './bookings.component';



@NgModule({
  declarations: [
    MyBookingsComponent,
    BookingsComponent
  ],
  imports: [
    CommonModule
  ]
})
export class BookingsModule { }
