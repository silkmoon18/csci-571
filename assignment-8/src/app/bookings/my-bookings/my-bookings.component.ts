import {Component, OnInit} from '@angular/core';
import {BookingsService} from "../bookings.service";

@Component({
  selector: 'app-my-bookings',
  templateUrl: './my-bookings.component.html',
  styleUrls: ['./my-bookings.component.css']
})
export class MyBookingsComponent implements OnInit {

  constructor(public bookingsService: BookingsService) {
  }

  bookings: any[] = []

  ngOnInit(): void {
    this.bookingsService.bookingsValue.subscribe(res => {
      if (!res)
        this.bookings = []
      else
        this.bookings = res
    })
  }

}
