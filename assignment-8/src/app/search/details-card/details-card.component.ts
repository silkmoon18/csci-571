import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {SearchService} from "../search.service";
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {DatePipe} from "@angular/common";
import {BookingsService} from "../../bookings/bookings.service";

@Component({
  selector: 'app-details-card',
  templateUrl: './details-card.component.html',
  styleUrls: ['./details-card.component.css']
})

export class DetailsCardComponent implements OnInit {

  constructor(private searchService: SearchService,
              public bookingService: BookingsService,
              private formBuilder: FormBuilder,
              private datePipe: DatePipe) {
  }

  @ViewChild('closeModal') closeModal!: ElementRef

  startChecking = false
  emailError = ['Email is required', 'Email must be a valid email address']

  columns: any[] = []

  name = ''
  photos = []
  url = ''

  details: Array<any>[] = []

  isHidden = true

  reservationForm = this.formBuilder.group({
    email: new FormControl('',[
      Validators.required,
      Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]),
    date: '',
    hour: '',
    minute: '',
  })
  get emailControl() {
    return this.reservationForm.get('email')!
  }

  hours = [10, 11, 12, 13, 14, 15, 16, 17]
  minutes = ['00', 15, 30, 45]

  mapOptions: google.maps.MapOptions = {
    center: { lat: 0, lng: 0 },
    zoom : 14,
  }
  marker = {
    position: { lat: 0, lng: 0 },
  }

  reviews: Array<any> = []

  ngOnInit(): void {
    this.searchService.detailCache.subscribe(res => {
      this.updateDetail(res)
    })
    this.searchService.display.subscribe((display: any) => {
      this.isHidden = !display.details
    })
    this.searchService.reviewsCache.subscribe(res => {
      this.reviews = Object.values(res)
    })
    this.clearDetail()
  }

  updateDetail(data: any) {
    const createDetail = (key: string, title: string) => {
      if (data[key] === '')
        return

      let h = document.createElement('h4')
      let p = document.createElement('p')

      h.append(title)

      switch (key) {
        case 'status':
          if (data[key] === 'Open Now')
            p.classList.add('text-success')
          else
            p.classList.add('text-danger')
          p.append(data[key])
          break
        case 'url':
          let a = document.createElement('a')
          a.append('Business link')
          a.href = data[key]
          a.target = '_blank'
          p.append(a)
          break
        default:
          p.append(data[key])
          break
      }

      this.details.push([h, p])
    }

    if (Object.keys(data).length === 0 || data === -1) {
      this.clearDetail()
      return
    }
    this.name = data.name
    this.photos = data.photos
    this.url = data.url
    this.searchService.updateReviews(data.id)

    this.mapOptions.center = {lat: data.coord.latitude, lng: data.coord.longitude}
    this.marker.position = {lat: data.coord.latitude, lng: data.coord.longitude}

    this.details = []
    createDetail('address', 'Address')
    createDetail('phone', 'Phone')
    createDetail('status', 'Status')
    createDetail('category', 'Category')
    createDetail('price', 'Price tag')
    createDetail('url', 'Visit yelp for more')

    let cols: any[] = []
    for (let i = 0; i < this.details.length; i += 3) {
      let col = document.createElement('div')
      cols.push(col)
    }

    for (let i = 0; i < this.details.length; i++) {
      let col = cols[Math.floor(i / 3)]

      let div = document.createElement('div')
      div.classList.add('row')
      div.append(this.details[i][0], this.details[i][1])

      col.append(div)
    }
    this.columns = cols

    this.isHidden = false
  }

  clearDetail(): void {
    this.details = []
    this.isHidden = true
  }

  onSubmit(): void {
    this.startChecking = true

    let isValid = true
    Object.keys(this.reservationForm.controls).forEach(key => {
      if (this.hasError(key))
        isValid = false
    });
    if (!isValid)
      return

    this.makeReservation()
  }

  private makeReservation(): void {
    let reservation = {
      name: this.name,
      date: this.reservationForm.get('date')?.value,
      time: this.reservationForm.get('hour')?.value + ":" + this.reservationForm.get('minute')?.value,
      email: this.reservationForm.get('email')?.value
    }
    this.bookingService.addReservation(reservation)

    this.closeModal.nativeElement.click()
  }

  cancelReservation(): void {
    this.bookingService.deleteReservation(this.bookingService.isReserved(this.name))
  }

  hasError(id: string): boolean {
    return this.startChecking && !!this.reservationForm.get(id)?.errors
  }

  back(): void {
    this.searchService.returnToTable()
  }

  getToday(): string {
    return <string>this.datePipe.transform(new Date(), 'yyyy-MM-dd')
  }
}
