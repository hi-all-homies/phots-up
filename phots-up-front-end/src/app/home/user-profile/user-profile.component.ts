import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserInfo } from 'src/app/model/user-info';
import { UserInfoService } from 'src/app/shared/user-info.service';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';
import { DataTransferService } from 'src/app/shared/data-transfer.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  @ViewChild('avatarInput') avatarInput: ElementRef;
  userInfo: UserInfo;
  avatar: File;
  avatarPreview: string;
  blankProfile: UserInfo;
  currUser: User;
  aboutMe: string;
  canEdit: boolean;

  constructor(
    private route: ActivatedRoute,
    private userService: UserInfoService,
    private auth: AuthService,
    private transferService: DataTransferService
    ) {
      this.auth.getCurrUser()
        .subscribe(u => this.currUser = u);
      
      this.transferService.getObservableUser()
        .subscribe(u =>{
          this.blankProfile =
            new UserInfo(-1, null, '', u, 'assets/logo/blank.png');
          this.canEdit = u.username === this.currUser.username});
    }

  ngOnInit(): void {
    let userId = this.route.snapshot.queryParamMap.get('user');
    this.userService.getUserInfoByUserId(userId)
      .subscribe(resp =>{
        if (resp)
          this.userInfo = resp;
        else
          this.userInfo = this.blankProfile
      });
  }

  saveChanges(){
    let newUserInfo = new UserInfo(
      null, this.userInfo.avatarKey, this.aboutMe, this.currUser, null);
    
    this.userService.saveUserInfo(newUserInfo, this.avatar)
      .subscribe(resp =>{
        this.userInfo.avatar = this.avatarPreview;
        this.userInfo.aboutMe = resp.body.aboutMe;
        this.userInfo.avatarKey = resp.body.avatarKey;
        this.avatar = null;
        this.avatarPreview = null;
        this.aboutMe = '';
      })
  }

  loadAvatar(){
    this.avatarInput.nativeElement.click();
  }

  showPreview(event: any){
    this.avatar = <File>event.target.files[0];

    if (this.avatar){
      let type = this.avatar.type;
      if (type.match(/image\/*/) == null)
        return;

      const reader = new FileReader();
      reader.onload = ev =>
        this.avatarPreview = <string> ev.target.result;

      reader.readAsDataURL(this.avatar);
    }
  }
  
  avatarUrl(): string{
	return `background-image: url(${this.userInfo.avatar});`
  }
}
