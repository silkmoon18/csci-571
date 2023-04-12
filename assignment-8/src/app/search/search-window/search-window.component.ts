import {Component, OnInit} from '@angular/core';
import {SearchService} from "../search.service";

import {FormBuilder} from '@angular/forms';
import {debounceTime, tap, switchMap, finalize, distinctUntilChanged, filter} from 'rxjs/operators';

@Component({
  selector: 'app-search-window',
  templateUrl: './search-window.component.html',
  styleUrls: ['./search-window.component.css']
})
export class SearchWindowComponent implements OnInit {

  constructor(private searchService: SearchService,
              private formBuilder: FormBuilder,) {
  }

  private detectedLocation = ''

  keyword = {} as HTMLInputElement
  distance = {} as HTMLInputElement
  category = {} as HTMLInputElement
  location = {} as HTMLInputElement
  autoLocated = {} as HTMLInputElement

  searchForm = this.formBuilder.group({
    keyword: '',
    distance: '10',
    category: 'All',
    location: '',
    autoLocated: false,
    detectedLocation: '',
  })

  isLoading = false
  options = []

  ngOnInit(): void {
    this.keyword = <HTMLInputElement>document.getElementById('keyword')
    this.distance = <HTMLInputElement>document.getElementById('distance')
    this.category = <HTMLInputElement>document.getElementById('category')
    this.location = <HTMLInputElement>document.getElementById('location')
    this.autoLocated = <HTMLInputElement>document.getElementById('autoLocated')

    this.searchForm.controls.keyword.valueChanges
      .pipe(
        filter(res => {
          return res !== null
        }),
        distinctUntilChanged(),
        debounceTime(1000),
        tap((value) => {
          if (value) {
            this.isLoading = true
          }
          else {
            this.isLoading = false
            this.options = []
          }
        })
      )
      .subscribe((data: any) => {
        if (!data)
          return

        this.searchService.getAutocompleteResult(data!.toString()).subscribe((res) => {
          this.isLoading = false
          if (res.length === 0) {
            this.options = [];
          } else {
            this.options = res;
          }
        })
      });
  }

  onLoad(): void {
    this.location.disabled = this.autoLocated.checked
  }

  detectClientLocation(enabled: boolean): void {
    if (enabled) {
      this.location.value = ""
      this.searchService.detectClientLocation().subscribe(
        response => {
          this.detectedLocation = response['loc'].split(',')
        })
    } else {
      this.detectedLocation = ''
    }
  }

  onSubmit(): void {
    if (!this.keyword.checkValidity() || !this.location.checkValidity()) {
      this.location.reportValidity()
      this.keyword.reportValidity()
      return
    }

    this.searchForm.controls['detectedLocation'].setValue(this.detectedLocation)

    this.searchService.updateSearchResult(JSON.stringify(this.searchForm.value))
    this.searchService.clearDetail()
  }

  onClear(): void {
    this.keyword.value = ''
    this.distance.value = '10'
    this.category.value = 'All'
    this.location.value = ''
    this.location.disabled = false
    this.searchForm.get('autoLocated')!.setValue(false)
    this.detectedLocation = ''

    this.searchService.clearSearchResult()
    this.searchService.clearDetail()
  }
}
