import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private router: Router,
              private route: ActivatedRoute) { }

  navIds = ['search', 'bookings']
  private navs : HTMLElement[] = []
  private selectedStyles = ["border", "border-dark", "border-3", "rounded-4"]
  private currentNav = -1

  ngOnInit(): void {
    for (let i = 0; i < this.navIds.length; i++) {
      this.navs.push(document.getElementById(this.navIds[i])!)
    }
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd))
      .subscribe(value => {
        let curr = this.navIds.indexOf(this.router.url.slice(1))
        this.setNav(curr)
      })
  }

  private setNav(index: number): void {
    if (this.currentNav == index)
      return

    this.currentNav = index
    for (let i = 0; i < this.navs.length; i++) {
      if (i == index) {
        this.navs[i].classList.add(...this.selectedStyles)
      }
      else {
        this.navs[i].classList.remove(...this.selectedStyles)
      }
    }
  }
}
