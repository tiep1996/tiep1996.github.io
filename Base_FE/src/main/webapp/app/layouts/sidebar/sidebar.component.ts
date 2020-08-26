import {AfterViewInit, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {JhiLanguageService} from 'ng-jhipster';
import {SessionStorageService} from 'ngx-webstorage';

import {VERSION} from 'app/app.constants';
import {ProfileService} from 'app/layouts/profiles/profile.service';
import {JhiLanguageHelper} from 'app/core/language/language.helper';
import {FormStoringService} from 'app/shared/services/form-storing.service';
import {TranslateService} from '@ngx-translate/core';
import {ToastService} from 'app/shared/services/toast.service';
import {CommonApiService} from 'app/core/services/common-api/common-api.service';
import {MENU_TITLE} from 'app/shared/constants/sidebar-menu.constants';
import {STORAGE_KEYS} from 'app/shared/constants/storage-keys.constants';
import {STATUS} from 'app/shared/constants/app-params.constants';
import {ProjectManagementService} from 'app/core/services/project-management/project-management.service';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit, AfterViewInit {
  inProduction: boolean;
  isNavbarCollapsed: boolean;
  languages: any[];
  swaggerEnabled: boolean;
  version: string;
  currentUser;
  hasPermission = false;

  systemManagements: any[] = [];
  categories: any[] = [];
  announcementManagements: any[] = [];
  invoiceManagements: any[] = [];
  reports: any[] = [];
  utilities: any[] = [];


  groupPermission = {
    name: MENU_TITLE.GROUP_PERMISSION.GP,
    url: '/system-categories/group-permissions',
    class: 'fa-street-view',
    resourceCode: 'menu.hddt_qlht_chdn'
  }
  humanResources = {
    name: MENU_TITLE.HRM.USER_MANAGEMENT,
    url: '/system-categories/human-resources',
    class: 'fa-user-plus',
    resourceCode: 'menu.hddt_qlht_chdn'
  }
  projectManager = {
    name: MENU_TITLE.HRM.PROJECT_MANAGEMENT,
    url: '/system-categories/project-management',
    class: 'fa-user-plus',
    resourceCode: 'menu.hddt_qlht_chdn'
  }



  systemCategories_ = [
    {
      name: MENU_TITLE.HRM.USER_MANAGEMENT,
      url: '/system-categories/human-resources',
      class: 'fa-user-plus',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.HRM.ADD_USER,
      url: '/system-categories/human-resources-add',
      class: 'fa-user-plus',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.HRM.EDIT_USER,
      url: '/system-categories/user-group-categories',
      class: 'fa-pencil-square-o',
      resourceCode: 'menu.hddt_qlht_nnd'
    },
    {
      name: MENU_TITLE.HRM.VIEWS_USER,
      url: '/system-categories/data-categories',
      class: 'fa-street-view',
      resourceCode: 'menu.hddt_qlht_nd'
    },
    {
      name: MENU_TITLE.HRM.USER_OF_PROJECT,
      url: '/system-categories/sys-user',
      class: 'fa-address-book-o',
      resourceCode: 'menu.hddt_qlht_nd'
    }
  ];


  project_author = [
    {
      name: MENU_TITLE.AUTHORITIES.VIEW_LIST,
      url: '/project-management/group-permission/show-list',
      class: 'fa-address-card-o',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.AUTHORITIES.ADD_Q,
      url: '/project-management/group-permission/add',
      class: 'fa-address-card-o',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.AUTHORITIES.EDIT_Q,
      url: '/project-management/group-permission/edit',
      class: 'fa-pencil',
      resourceCode: 'menu.hddt_qlht_chdn'
    }
  ];


  project_management = [
    {
      name: MENU_TITLE.HRM.PROJECT_MANAGEMENT,
      url: '/system-categories/project-management',
      class: 'fa-user-plus',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.HRM.ADD_PROJECT,
      url: '/system-categories/add-user',
      class: 'fa-user-plus',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.HRM.ADD_USER,
      url: '/system-categories/human-resources',
      class: 'fa-user-plus',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.HRM.EDIT_USER,
      url: '/system-categories/organization-categories',
      class: 'fa-user-plus',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.HRM.VIEWS_USER,
      url: '/system-categories/view-user',
      class: 'fa-user-plus',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.PROJECT_MANAGEMENT.ADD_EMP_PROJECT,
      url: '/system-categories/masterplan',
      class: 'fa-file',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
    {
      name: MENU_TITLE.PROJECT_MANAGEMENT.ADD_EMP_PROJECT,
      url: '/system-categories/project-management/add-human',
      class: 'fa-file',
      resourceCode: 'menu.hddt_qlht_chdn'
    },
  ];

  temp = [
    {
      name: MENU_TITLE.TEMP.USER,
      url: '/temp/system-user',
      class: 'fa-user-plus',
      resourceCode: 'menu.hddt_qlht_chdn'
    }
  ];
  menuFunctionList: any[] = [];
  menuFunctionListNotAuth: any[] = [];

  constructor(
    private languageService: JhiLanguageService,
    private languageHelper: JhiLanguageHelper,
    private sessionStorage: SessionStorageService,
    private profileService: ProfileService,
    private router: Router,
    private formStoringService: FormStoringService,
    private toastService: ToastService,
    private translateService: TranslateService,
    private commonApiService: CommonApiService,
    private cdref: ChangeDetectorRef,
    private projectManagementService: ProjectManagementService
  ) {
    this.version = VERSION ? 'v' + VERSION : '';
    this.isNavbarCollapsed = true;
  }

  ngOnInit() {
    this.languages = this.languageHelper.getAll();
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.swaggerEnabled = profileInfo.swaggerEnabled;
    });
  }

  ngAfterViewInit(): void {
    this.currentUser = this.formStoringService.get(STORAGE_KEYS.CURRENT_USER);
    if (!this.currentUser) {
      this.hasPermission = false;
      // this.setPermission();
      //this.getMenuFunctionNotAuth();
      this.cdref.detectChanges();
      return;
    } else {
      if (this.currentUser.userState === STATUS.ACTIVE && this.currentUser.tenantState === STATUS.ACTIVE) {
        this.hasPermission = true;
        // this.getMenuFunction();
        this.cdref.detectChanges();
      }
    }
  }

  changeLanguage(languageKey: string) {
    this.sessionStorage.store('locale', languageKey);
    this.languageService.changeLanguage(languageKey);
  }

  collapseNavbar() {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated() {
  }

  login() {
  }

  logout() {
    this.collapseNavbar();
    this.router.navigate(['']);
  }

  toggleNavbar() {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl() {
  }

  setPermission() {
    this.getActiveRouter(this.systemCategories_, 0);
  }

  getActiveRouter(arr: any[] = [], number: any) {
    for (let index = 0; index < arr.length; index++) {
      if (number === 0) {
        this.systemManagements.push(arr[index]);
      } else if (number === 1) {
        this.categories.push(arr[index]);
      } else if (number === 2) {
        this.announcementManagements.push(arr[index]);
      } else if (number === 3) {
        this.invoiceManagements.push(arr[index]);
      } else if (number === 4) {
        this.reports.push(arr[index]);
      } else if (number === 5) {
        this.utilities.push(arr[index]);
      }
    }
  }

  /* public getMenuFunctionNotAuth() {
    this.commonApiService.getMenuFunctionNotAuth().subscribe(res => {
      if (res && res.body.code === STATUS_CODE.SUCCESS) {
        this.menuFunctionListNotAuth = res.body.data;
      } else {
        this.menuFunctionListNotAuth = [];
      }
    }, () => {
      // this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
    });
  }*/

  /* public getMenuFunction() {
    this.commonApiService.getMenuFunction().subscribe(res => {
      if (res && res.body.code === STATUS_CODE.SUCCESS) {
        this.menuFunctionList = res.body.data;
      } else {
        this.menuFunctionList = [];
      }
    }, () => {
      // this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
    });
  }*/

  filterParent(menuFunctionList) {
    if (menuFunctionList && menuFunctionList.length > 0) {
      const result = menuFunctionList.filter(it => it.parentId === null);
      return result;
    }
  }

  filterDetails(menuFunctionList, data) {
    if (menuFunctionList && menuFunctionList.length > 0) {
      return menuFunctionList.filter(it => it.parentId === data.id);
    }
  }

  clearCacheOutsource() {
    this.projectManagementService.navigation(null);
  }
}
