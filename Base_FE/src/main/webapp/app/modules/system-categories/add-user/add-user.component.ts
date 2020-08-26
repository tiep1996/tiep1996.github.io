import {Component, EventEmitter, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { ITEMS_PER_PAGE, MAX_SIZE_PAGE } from 'app/shared/constants/pagination.constants';
import {ActivatedRoute, Router} from '@angular/router';
import { HeightService } from 'app/shared/services/height.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmModalComponent } from 'app/shared/components/confirm-modal/confirm-modal.component';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { OrganizationCategoriesService } from 'app/core/services/system-management/organization-categories.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, of, Subject, Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { debounceTime } from 'rxjs/operators';
import { TIME_OUT } from 'app/shared/constants/set-timeout.constants';
import { ToastService } from 'app/shared/services/toast.service';
import { CommonService } from 'app/shared/services/common.service';
import { REGEX_PATTERN } from 'app/shared/constants/pattern.constants';
import {OrganizationCategoriesModel} from "app/core/models/system-categories/organization-categories.model";
import {CommonUtils} from "app/shared/util/common-utils.service";
import {STATUS_CODE} from "app/shared/constants/status-code.constants";
import {UploadFileComponent} from "app/shared/components/upload-file/upload-file.component";
import {AttachDocument} from "app/core/models/project-management/attach-document";
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";

@Component({
  selector: 'jhi-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {
  @ViewChild('fileImport', { static: false }) fileImport: UploadFileComponent;
  @Input() public selectedData;
 // @Input() type;
  type: any;
  form: FormGroup;
  height: number;
  itemsPerPage: any;
  maxSizePage: any;
  routeData: any;
  page: any;
  filePerPage = 10;
  dateUpload:any;
  filePage = 1;
  maxFilePage = 10;
  listFileProject: any;
  second: any;
  totalItems: any;
  previousPage: any;
  buttonDisable;
  predicate: any;
  reverse: any;
  checkDelete = false;
  listId: any[];
  listA: any[];
  checkCode:false;
  checkSpace = false;
  listUnit$ = new Observable<any[]>();
  parentOrganizationList = new Observable<any[]>();
  // groupOrganizationList = new Observable<any[]>();
  groupOrganizationList: any[] = [];
  unitSearch;
  debouncer: Subject<string> = new Subject<string>();
  listUnit1$ = new Observable<any[]>();
  unitSearch1;
  debouncer1: Subject<string> = new Subject<string>();
  listUnit2$ = new Observable<any[]>();
  unitSearch2;
  debouncer2: Subject<string> = new Subject<string>();
  listUnit3$ = new Observable<any[]>();
  unitSearch3;
  debouncer3: Subject<string> = new Subject<string>();
  listUnit4$ = new Observable<any[]>();
  unitSearch4;
  debouncer4: Subject<string> = new Subject<string>();
  listUnit5$ = new Observable<any[]>();
  unitSearch5;
  debouncer5: Subject<string> = new Subject<string>();
  searchForm: any;
  eventSubscriber: Subscription;
  organizationCategoriesModel:[];
  organizationList: any[];
  rBaM:number ;
  rpmT:number ;
  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  message = '';
  attachDoc = [];
  testDoc: OrganizationCategoriesModel;
  lstFile = [];
  // import
  errImport = false;
  successImport = false;
  successMessage;
  errMessage;

  constructor(
    private activatedRoute: ActivatedRoute,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private translateService: TranslateService,
    private spinner: NgxSpinnerService,
    private toastService: ToastService,
    private organizationCategoriesService: OrganizationCategoriesService,
    private formStoringService: FormStoringService,
    private eventManager: JhiEventManager,
    private _http: HttpClient,
    protected router: Router,
    private commonService: CommonService,
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.maxSizePage = MAX_SIZE_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      if (data && data.pagingParams) {
        this.page = data.pagingParams.page;
        this.previousPage = data.pagingParams.page;
        this.reverse = data.pagingParams.ascending;
        this.predicate = data.pagingParams.predicate;
      }
    });
  }

  ngOnInit() {
    this.searchForm = {};
    this.buidForm();
    this.onResize();
    this.searchHandle();
    this.registerChange();
    // this.debounceOnSearch();
    this.debounceOnSearch1();
    this.debounceOnSearch2();
    this.debounceOnSearch3();
    this.debounceOnSearch4();
    this.debounceOnSearch5();
    this.rBaM=1;
    this.rpmT=0;
  }


  selectFile(event) {
    this.selectedFiles = event.target.files;
  }


  // upload() {
  //   this.progress = 0;
  //
  //   this.currentFile = this.selectedFiles.item(0);
  //   console.warn('2222', this.currentFile);
  //   this.organizationCategoriesService.upload(this.currentFile).subscribe(
  //     event => {
  //       console.warn('1', event);
  //       this.testDoc = {
  //         lstAttachDocument: {
  //           name: this.currentFile.name,
  //
  //           //createDate:this.currentFile.lastModified,
  //         },
  //       }
  //
  //     });
  //   this.attachDoc.push(this.testDoc.lstAttachDocument);
  //
  //   this.selectedFiles = undefined;
  // }



  onSearchUnit(event) {
    this.unitSearch = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer.next(term);
    } else {
      this.listUnit$ = of([]);
    }
  }

  // debounceOnSearch() {
  //   this.debouncer.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit(value));
  // }
  // loadDataOnSearchUnit(term) {
  //   this.organizationCategoriesService.getGroups(term).subscribe((res: HttpResponse<any[]>) => {
  //     if (this.unitSearch) {
  //       this.listUnit$ = of(res['content']);
  //     } else {
  //       this.listUnit$ = of([]);
  //     }
  //   });
  // }
  onClearUnit() {
    this.listUnit$ = of([]);
    this.unitSearch = '';
  }

  onSearchUnitClose() {
    if (!this.form.value.groupName) {
      this.listUnit$ = of([]);
      this.unitSearch = '';
    }
  }

  onSearchUnit1(event) {
    this.unitSearch1 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer1.next(term);
    } else {
      this.listUnit1$ = of([]);
    }
  }
  debounceOnSearch1() {
    this.debouncer1.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit1(value));
  }
  loadDataOnSearchUnit1(term) {
    // eslint-disable-next-line no-console
    console.log(term);
    const data = {
      keySearch: term,
      type: 'PM'
    }
    this.organizationCategoriesService.getParents(data).subscribe((res => {
      if (this.unitSearch1) {
        const dataRes: any = res;
        this.listUnit1$ = of(dataRes.sort((a, b) => a.username.localeCompare(b.username)));
      } else {
        this.listUnit1$ = of([]);
      }
    }));
  }
  onClearUnit1() {
    this.listUnit1$ = of([]);
    this.unitSearch1 = '';
  }

  onSearchUnitClose1() {
    if (!this.form.value.name) {
      this.listUnit1$ = of([]);
      this.unitSearch1 = '';
    }
  }
  customSearchFn1(term: string, item: any): any {
    term = term.toLocaleLowerCase();
    return item.username.toLocaleLowerCase().indexOf(term) > -1 ||
      item.email.toLocaleLowerCase().indexOf(term) > -1;
  }



  onSearchUnit2(event) {
    // eslint-disable-next-line no-debugger
    this.unitSearch2 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer2.next(term);
    } else {
      this.listUnit2$ = of([]);
    }
  }
  debounceOnSearch2() {
    this.debouncer2.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit2(value));
  }
  loadDataOnSearchUnit2(term) {
    // eslint-disable-next-line no-debugger
    const data = {
      keySearch: term,
      type: 'BM'
    }

    this.organizationCategoriesService.getParents(data).subscribe(res =>   {
      if (this.unitSearch2) {
        const dataRes: any = res;
        this.listUnit2$ = of(dataRes.sort((a, b) => a.username.localeCompare(b.username)));
      } else {
        this.listUnit2$ = of([]);
      }
    });
  }
  onClearUnit2() {
    this.listUnit2$ = of([]);
    this.unitSearch2 = '';
  }

  onSearchUnitClose2() {
    if (!this.form.value.name) {
      this.listUnit2$ = of([]);
      this.unitSearch2 = '';
    }
  }

  onSearchUnit3(event) {
    this.unitSearch3 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer3.next(term);
    } else {
      this.listUnit3$ = of([]);
    }
  }
  debounceOnSearch3() {
    this.debouncer3.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit3(value));
  }
  loadDataOnSearchUnit3(term) {
    const data = {
      keySearch: term,
      type: 'TL'
    }

    this.organizationCategoriesService.getParents(data).subscribe(res =>   {
      if (this.unitSearch3) {
        const dataRes: any = res;
        this.listUnit3$ = of(dataRes.sort((a, b) => a.username.localeCompare(b.username)));
      } else {
        this.listUnit3$ = of([]);
      }
    });
  }
  onClearUnit3() {
    this.listUnit3$ = of([]);
    this.unitSearch3 = '';
  }
  onSearchUnitClose3() {
    if (!this.form.value.name) {
      this.listUnit3$ = of([]);
      this.unitSearch3 = '';
    }
  }


  onSearchUnit4(event) {
    this.unitSearch4 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer4.next(term);
    } else {
      this.listUnit4$ = of([]);
    }
  }
  debounceOnSearch4() {
    this.debouncer4.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit4(value));
  }
  loadDataOnSearchUnit4(term) {
    const data = {
      keySearch: term,
      type: 'QM'
    }

    this.organizationCategoriesService.getParents(data).subscribe(res =>   {
      if (this.unitSearch4) {
        const dataRes: any = res;
        this.listUnit4$ = of(dataRes.sort((a, b) => a.username.localeCompare(b.username)));
      } else {
        this.listUnit4$ = of([]);
      }
    });
  }
  onClearUnit4() {
    this.listUnit4$ = of([]);
    this.unitSearch4 = '';
  }

  onSearchUnitClose4() {
    if (!this.form.value.parentName) {
      this.listUnit4$ = of([]);
      this.unitSearch4 = '';
    }
  }


  onSearchUnit5(event) {
    this.unitSearch5 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer5.next(term);
    } else {
      this.listUnit5$ = of([]);
    }
  }
  debounceOnSearch5() {
    this.debouncer5.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit5(value));
  }
  loadDataOnSearchUnit5(term) {
    const data = {
      keySearch: term,
      type: 'PARTNER'
    }

    this.organizationCategoriesService.getPartnerInfo(data).subscribe(res =>   {
      if (this.unitSearch5) {
        const dataRes: any = res;
        this.listUnit5$ = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));
      } else {
        this.listUnit5$ = of([]);
      }
    });
  }
  onClearUnit5() {
    this.listUnit5$ = of([]);
    this.unitSearch5 = '';
  }

  onSearchUnitClose5() {
    if (!this.form.value.parentName) {
      this.listUnit5$ = of([]);
      this.unitSearch5 = '';
    }
  }

  customSearchFn(term: string, item: any): any {
    term = term.toLocaleLowerCase();
    return item.code.toLocaleLowerCase().indexOf(term) > -1 ||
      item.name.toLocaleLowerCase().indexOf(term) > -1;
  }


  searchHandle() {
    this.page = this.page - 1;
    this.organizationCategoriesService.search(this.searchForm).subscribe(result => {
      this.organizationCategoriesModel = result.data;
      this.totalItems = result.dataCount;
    });
  }



  onChangePosition(event) {
    if (event) {
      this.setValueToField('dataCategoryId', event.dataCategoryId);
      this.setValueToField('groupName', event.groupName);
    }
  }

  onClearPosition() {
    this.setValueToField('dataCategoryId', null);
    this.setValueToField('groupName', null);
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  fetchData() {
    this.organizationCategoriesService.search(this.searchForm).subscribe(result => {
      this.organizationCategoriesModel = result.data;
      this.totalItems = result.dataCount;
    });
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  convertDate(str) {
    const date = new Date(str),
      mnth = ('0' + (date.getMonth() + 1)).slice(-2),
      day = ('0' + date.getDate()).slice(-2);
    return [ day, mnth ,date.getFullYear()].join('/');
  }

  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('organizationChange', response => this.searchHandle());
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      code: [null, Validators.compose([Validators.required])],
      name: [null, Validators.compose([Validators.required])],
      pmId: [null, Validators.compose([Validators.required])],
      baId: [null, Validators.compose([Validators.required])],
      qaId: [null, Validators.compose([Validators.required])],
      testLeadId:[null, Validators.compose([Validators.required])],
      customerPmName:[null, Validators.compose([Validators.required])],
      customerPmEmail:[''],
      amName:[''],
      amEmail:[''],
      Ba:[],
      description:[],
      partnerId:[null, Validators.compose([Validators.required])],
      ba:[],
      dev:[],
      test:[],
      baM:[],
      taT:[],
      amPhone:[],
      customerPmPhone:[],
      listFileProject:['']

    });
  }

  onProfitSelectionChange(data) : void {
    if ('BA' === data) {
      this.rBaM = 1;
      this.rpmT = 0;
    }

    if ('Lead' === data) {
      this.rBaM = 0;
      this.rpmT = 1;
    }
  };

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  get formControl() {
    return this.form.controls;
  }


  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }


  checkCodeExist(code) {
    //algorithm follows
    const r = /^[A-Za-z0-9_.]+$/
    if(!r.test(this.form.value.code)){
      this.checkSpace = true;
      this.checkCode = false;
    }else{
      this.checkSpace=false;
      this.organizationCategoriesService.checkCodeExist(this.form.value.code).subscribe(
        res => {
          this.checkCode=res.body;

        },
        error => {

        })
    }


  }

  getValueOfField(item) {
    return this.form.get(item).value;
  }
  onBlurEmail(field) {
    this.setValueToField(field, this.getValueOfField(field).trim());
    if (!REGEX_PATTERN.EMAIL.test(this.getValueOfField(field))) {
      if (this.getValueOfField(field) !== '') {
        this.form.controls[field].setErrors({ invalid: true });
      }
    }
  }

  saveProject() {
    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      return;
    }
    const userToken: any = this.formStoringService.get(STORAGE_KEYS.USER);
    const data = {
      pmId: this.form.value.pmId,
      code: this.form.value.code.trim(),
      name: this.form.value.name.trim(),
      baId: this.form.value.baId,
      qaId: this.form.value.qaId,
      testLeadId: this.form.value.testLeadId,
      customerPmName: this.form.value.customerPmName.trim(),
      customerEmail: this.form.value.customerPmEmail,
      customerPmPhone: this.form.value.customerPmPhone,
      amName: this.form.value.amName,
      amEmail: this.form.value.amEmail,
      amPhone: this.form.value.amPhone,
      description: this.form.value.description,
      partnerID: this.form.value.partnerId,
      ba: this.form.value.ba === true ? 1 : 0,
      dev: this.form.value.dev === true ? 1 : 0,
      test: this.form.value.test === true ? 1 : 0,
      // baMan: this.form.value.baM=== true ? 1 : 0,
      // pmMan: this.form.value.taT=== true ? 1 : 0,
      baMan: this.rBaM,
      humanResourcesId : userToken.humanResourceId,
      // pmMan: this.rpmT,
      // lstAttachDocument: [{
      //   code: 1,
      //   name: this.currentFile === undefined ? "none" : this.currentFile.name,
      // }],
     // lstAttachFile: this.lstFile,

    }


      const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
      modalRef.componentInstance.type = 'create';
      modalRef.componentInstance.param = 'dự án';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.organizationCategoriesService.saveProject(data, this.listFileProject).subscribe(res => {
            console.warn('edit', data);
            this.toastService.openSuccessToast('Thêm mới dữ liệu thành công!');
            this.router.navigate(['system-categories/project-management']);
          });
        }
      });


  }

  cancel(){
    const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
    modalRef.componentInstance.type = 'close';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      this.router.navigate(['system-categories/project-management']);
    });
  }

  onChangeFile(event: EventEmitter<File[]>) {
    this.dateUpload = this.convertDate(new Date());
    this.listFileProject = event;
    this.checkSizeFile();
  }

  onError(event) {
    if (event === '') {
      this.errImport = false;
      this.successImport = true;
      this.successMessage = this.translateService.instant('common.import.success.upload');
    } else {
      this.errImport = true;
      this.successImport = false;
      this.errMessage = event;
    }
  }

  onDeleteDocument(item) {
    let status;
    if (item.id !== undefined) {
      // this.fileAttachmentService.deleteFile(item.id).subscribe(
      //   res => {
      //     if (res.status === STATUS_CODE.SUCCESS) {
      //       status = res.status;
      //       this.toastService.openSuccessToast(this.translateService.instant('outsourcingPlan.toastr.delete.successful'));
      //     }
      //   },
      //   error => {
      //     this.toastService.openErrorToast(this.translateService.instant('outsourcingPlan.toastr.delete.fail'));
      //   },
      //   () => {
      //     let data;
      //     if (status === STATUS_CODE.SUCCESS) {
      //       data = this.formControl.listFileProject.value.filter(value => {
      //         if (value.fileName !== undefined && item.fileName !== undefined) {
      //           return value.fileName !== item.fileName;
      //         }
      //         if (value.name !== undefined) {
      //           return value;
      //         }
      //       });
      //     } else {
      //       data = this.formControl.listFileProject.value.filter(value => {
      //         if (value.fileName !== undefined && item.fileName !== undefined) {
      //           return value.fileName;
      //         }
      //         if (value.name !== undefined) {
      //           return value;
      //         }
      //       });
      //     }
      //     this.setValueToField('listFileProject', data);
      //   }
      // );
    } else {
      const data = this.formControl.listFileProject.value.filter(value => {
        if (value.name !== undefined && item.name !== undefined) {
          return value.name !== item.name;
        }
        if (value.fileName !== undefined) {
          return value;
        }
      });
      this.listFileProject = data;
      this.setValueToField('listFileProject', data);
      this.checkSizeFile();
    }
  }

  pageChange(event) {
    this.filePage = event;
  }


  downloadFile(item) {
    // if (item.id !== undefined) {
    //   this.spinner.show();
    //   this.fileAttachmentService.downloadFile(item.filePath).subscribe(res => {
    //     this.spinner.hide();
    //     if (res) {
    //       this.downloadService.downloadFile(res);
    //     }
    //   });
    // }
  }

  changePageSize(size) {
    this.filePerPage = size;
    // this.transition();
  }

  checkSizeFile() {
    let i = 0;
    for (i = 0; i < this.listFileProject.length; i++) {
      if (CommonUtils.tctGetFileSize(this.listFileProject[i]) > 5) {
        this.form.controls['listFileProject'].setErrors({maxFile: true});
      }
    }
  }
}
