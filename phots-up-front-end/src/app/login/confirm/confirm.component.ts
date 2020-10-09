import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/shared/auth.service';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.css']
})
export class ConfirmComponent implements OnInit, AfterViewInit {
  @ViewChild('confirmed')
  private confirmed: ElementRef;

  @ViewChild('fail')
  private fail: ElementRef;

  private confirm: Observable<Boolean>;

  constructor(private route: ActivatedRoute, private auth: AuthService) {}

  ngAfterViewInit(): void {
    this.confirm.subscribe(result => {
      if (result)
        this.confirmed.nativeElement.style.display = 'initial'
      else 
        this.fail.nativeElement.style.display = 'initial'
    })
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      paramMap => this.confirm = this.auth.confirmCode(paramMap.get('code')))
  }
}