import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BookingsService {

  constructor() {}

  bookingsValue = new BehaviorSubject(this.bookings);

  set bookings(value: Array<any>) {
    this.bookingsValue?.next(value)
    localStorage.setItem('bookings', JSON.stringify(value))
  }

  get bookings() {
    let result = JSON.parse(localStorage.getItem('bookings')!)
    if (!result) {
      result = []
      this.bookings = result
    }
    return result
  }

  addReservation(reservation: object): void {
    if (!this.bookings)
      this.bookings = []

    let temp = this.bookings
    temp.push(reservation)
    this.bookings = temp

    alert('Reservation created!')
  }

  deleteReservation(index: number): void {
    if (index < 0)
      return

    let temp = this.bookings
    temp.splice(index, 1)
    this.bookings = temp

    alert('Reservation cancelled!')
  }

  isReserved(name: string): number {
    let temp = this.bookings

    for (let booking of temp) {
      if (booking['name'] === name)
        return temp.indexOf(booking)
    }
    return -1
  }
}
