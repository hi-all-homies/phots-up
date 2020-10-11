import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[AddConfirm]'
})
export class AddConfirmDirective {

  constructor(public viewContainerRef: ViewContainerRef) {}

}
