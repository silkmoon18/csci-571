import { Component, OnInit } from '@angular/core';
import {SearchService} from "../search.service";

@Component({
  selector: 'app-result-table',
  templateUrl: './result-table.component.html',
  styleUrls: ['./result-table.component.css']
})
export class ResultTableComponent implements OnInit {

  constructor(private searchService: SearchService) { }

  tableData: any[] | undefined
    isHidden = true

  ngOnInit(): void {
    this.searchService.tableDataCache.subscribe(res => {
      this.updateTableData(res)
    })
    this.searchService.display.subscribe((display: any) => {
      this.isHidden = !display.table
    })
    this.clearTable()
  }

  updateTableData(data: any): void {
    if (data === -1) {
      this.clearTable()
      return
    }

    this.isHidden = false
    this.tableData = Object.keys(data).map((key) => [Number(key) + 1, data[key]])
  }

  isDataEmpty() {
    return !this.tableData || this.tableData.length === 0
  }

  clearTable() {
    this.isHidden = true
    this.tableData = []
  }

  displayBusinessDetail(id: string) {
    this.searchService.updateSelectedDetail(id)
  }
}
