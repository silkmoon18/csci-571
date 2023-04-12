import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from "rxjs";

const root = ''
const IPINFO_URL = 'https://ipinfo.io/?token=ddf760ed1e2c25'
@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private http: HttpClient) { }

  private httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  private result = new BehaviorSubject({} as Object)
  tableDataCache = this.result.asObservable()

  private detail = new BehaviorSubject({} as Object)
  detailCache = this.detail.asObservable()

  private reviews = new BehaviorSubject({} as Object)
  reviewsCache = this.reviews.asObservable()

  private displayProperty = {
    table: false,
    details: false,
  }
  private displayBehavior = new BehaviorSubject(this.displayProperty)
  display = this.displayBehavior.asObservable()



  getAutocompleteResult(keyword: string): Observable<any> {
    return this.http.get(root + '/handle_autocomplete?keyword=' + keyword, this.httpOptions);
  }

  detectClientLocation(): Observable<any> {
    return this.http.get(IPINFO_URL, this.httpOptions);
  }

  updateSearchResult(params: Object): void {
    this.http.get(root + '/handle_input?params=' + params, this.httpOptions)
      .subscribe(res => {
        this.result.next(res)
      })
  }

  clearSearchResult(): void {
    this.result.next(-1)
  }

  updateSelectedDetail(id: string): void {
    this.http.get(root + '/get_business?id=' + id, this.httpOptions)
      .subscribe(res => {
        this.detail.next(res)

        this.displayProperty.table = false
        this.displayProperty.details = true
        this.displayBehavior.next(this.displayProperty)
      })
  }

  clearDetail(): void {
    this.detail.next(-1)
  }

  returnToTable(): void {
    this.displayProperty.table = true
    this.displayProperty.details = false
    this.displayBehavior.next(this.displayProperty)
  }

  updateReviews(id: string): void {
    this.http.get(root + '/get_reviews?id=' + id, this.httpOptions)
      .subscribe(res => {
        this.reviews.next(res)
      })
  }
}
