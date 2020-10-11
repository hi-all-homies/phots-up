import { Component, ComponentFactoryResolver, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/shared/auth.service';
import { AddConfirmDirective } from './add-confirm.directive';
import { ResultComponent } from './result/result.component';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.css']
})
export class ConfirmComponent implements OnInit{
  @ViewChild(AddConfirmDirective, {static: true}) anchor: AddConfirmDirective;

  constructor(
    private componentFactoryResolver: ComponentFactoryResolver,
    private route: ActivatedRoute,
    private auth: AuthService){}

  ngOnInit(){
    this.route.paramMap.subscribe(params => {
      let code = params.get('code');
      this.auth.confirmCode(code)
        .subscribe(result => this.loadComponent(result))
      })
  }

  private loadComponent(result: boolean){
    let view = {
      message: '',
      color: ''
    };
    if (result){
      view.message = 'Email was confirmed, you can log in';
      view.color = 'limegreen';
    }
    else{
      view.message = "failure occurs...";
      view.color = 'crimson';
    }

    const componentFactory =
      this.componentFactoryResolver.resolveComponentFactory(ResultComponent);

    const vcr = this.anchor.viewContainerRef;
    const component = vcr.createComponent<ResultComponent>(componentFactory);
    component.instance.view = view;
  }
}