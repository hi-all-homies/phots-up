import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserInfo } from 'src/app/model/user-info';
import { UserInfoService } from 'src/app/shared/user-info.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  readonly blankAvatar: string = 'assets/logo/Blank-avatar.png';
  userInfo: UserInfo;

  constructor(
    private route: ActivatedRoute,
    private userService: UserInfoService
    ) {}

  ngOnInit(): void {
    let userId = this.route.snapshot.queryParamMap.get('user');
    this.userService.getUserInfoByUserId(userId)
      .subscribe(resp =>{
        if (resp.body)
          this.userInfo = resp.body;
        else {
          let blank = new UserInfo();
          blank.avatar = this.blankAvatar;
          blank.aboutMe = 'empty';
          this.userInfo = blank;
        }
      })
  }

  saveChanges(){}
}
