import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';

import {SearchModule} from "./search/search.module";
import {BookingsModule} from "./bookings/bookings.module";
import {NavbarComponent} from './navbar/navbar.component';
import {TestComponent} from './test/test.component';
import {RouterOutlet} from "@angular/router";
import {AppRoutingModule} from "./routing/routing.module";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {ReactiveFormsModule} from "@angular/forms";

import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from '@angular/material/input';
import {DatePipe} from "@angular/common";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    TestComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    SearchModule,
    BookingsModule,
    RouterOutlet,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule {
}
