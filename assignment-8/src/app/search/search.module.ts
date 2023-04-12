import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SearchWindowComponent} from "./search-window/search-window.component";
import {ReactiveFormsModule} from "@angular/forms";
import {ResultTableComponent} from './result-table/result-table.component';
import {SearchComponent} from './search.component';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {DetailsCardComponent} from './details-card/details-card.component';
import {MatTabsModule} from "@angular/material/tabs";
import {GoogleMapsModule} from "@angular/google-maps";


@NgModule({
  declarations: [
    SearchWindowComponent,
    ResultTableComponent,
    SearchComponent,
    DetailsCardComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatProgressSpinnerModule,
    MatTabsModule,
    GoogleMapsModule
  ]
})
export class SearchModule {
}
