import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserInfo } from 'src/app/model/user-info';
import { UserInfoService } from 'src/app/shared/user-info.service';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';
import { MatExpansionPanel } from '@angular/material/expansion';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  @ViewChild('avatarInput') avatarInput: ElementRef;
  @ViewChild(MatExpansionPanel) panel: MatExpansionPanel;
  userInfo: UserInfo;
  avatar: File;
  avatarPreview: string;
  readonly blankAvatar: string = 'assets/logo/blank.png';
  currUser: User;
  aboutMe: string;
  canEdit: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private userService: UserInfoService,
    private auth: AuthService
    ) {}

  ngOnInit(): void {
    this.auth.getCurrUser()
      .subscribe(u => this.currUser = u);
    let userId = this.route.snapshot.queryParamMap.get('user');
    this.getUserInfo(userId);
  }


  private getUserInfo(userId: any){
    this.userService.getUserInfoByUserId(userId)
      .subscribe(userInfo =>{
        if (userInfo.avatar == null)
          userInfo.avatar = this.blankAvatar;
        if (userInfo.aboutMe == null)
          userInfo.aboutMe = '';
        this.userInfo = userInfo;
        this.canEdit = userInfo.user.username === this.currUser.username;
      })
  }


  saveChanges(){
    let newUserInfo = new UserInfo(
      null, this.userInfo.avatarKey, this.aboutMe, this.currUser, null);
    
    this.userService.saveUserInfo(newUserInfo, this.avatar)
      .subscribe(resp =>{
		if (this.avatarPreview)
			this.userInfo.avatar = this.avatarPreview;
        this.userInfo.aboutMe = resp.body.aboutMe;
        this.userInfo.avatarKey = resp.body.avatarKey;
        this.avatar = null;
        this.avatarPreview = null;
        this.aboutMe = '';
        this.panel.close();
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
