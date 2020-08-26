import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from 'app/shared/constants/pagination.constants';
import {ActivatedRoute, Router} from '@angular/router';
import {HeightService} from 'app/shared/services/height.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {InvoiceSerialModel} from 'app/core/models/announcement-management/invoice-serial.model';
import {ConfirmModalComponent} from 'app/shared/components/confirm-modal/confirm-modal.component';
import {TranslateService} from '@ngx-translate/core';
import {NgxSpinnerService} from 'ngx-spinner';
import {OrganizationCategoriesService} from 'app/core/services/system-management/organization-categories.service';
import {OrganizationCategoriesModel} from 'app/core/models/system-categories/organization-categories.model';
import {HttpClient} from '@angular/common/http';
import {SHOW_HIDE_COL_HEIGHT} from 'app/shared/constants/perfect-scroll-height.constants';
import {BehaviorSubject, Observable, of, Subject, Subscription} from 'rxjs';
import {JhiEventManager} from 'ng-jhipster';
import {debounceTime} from 'rxjs/operators';
import {TIME_OUT} from 'app/shared/constants/set-timeout.constants';
import {ToastService} from 'app/shared/services/toast.service';
import {REGEX_PATTERN} from 'app/shared/constants/pattern.constants';
import {ListHandoverProductComponent} from "app/modules/system-categories/project-management/list-handover-product/list-handover-product.component";
import {CommonService} from "app/shared/services/common.service";
import {AddHumanComponent} from "app/modules/system-categories/project-management/add-human/add-human.component";
import {FormStoringService} from "app/shared/services/form-storing.service";


@Component({
  selector: 'jhi-project-management',
  templateUrl: './project-management.component.html',
  styleUrls: ['./project-management.component.scss']
})
export class ProjectManagementComponent implements OnInit {
  @Input() public selectedData: InvoiceSerialModel;
  @Input() type;
  form: FormGroup;
  token:string;
  height: number;
  itemsPerPage: any;
  maxSizePage: any;
  pageSize: any;
  routeData: any;
  page: any;
  buttonDisable;
  second: any;
  totalItems: any;
  checkSearch = false;
  startDate: number;
  endDate: number;
  message;
  previousPage: any;
  humanId:any;
  messageDate = null;
  predicate: any;
  reverse: any;
  checkDelete = false;
  listId: any[];
  columns:any[];
  listA: any[];
  listUnit$ = new Observable<any[]>();
  parentOrganizationList = new Observable<any[]>();
  // groupOrganizationList = new Observable<any[]>();
  groupOrganizationList: any[] = [];
  groupOrganizationList2: any[] = [];
  unitSearch;
  debouncer: Subject<string> = new Subject<string>();
  listUnit1$ = new Observable<any[]>();
  unitSearch1;
  debouncer1: Subject<string> = new Subject<string>();
  listUnit2$ = new Observable<any[]>();
  unitSearch2;
  debouncer2: Subject<string> = new Subject<string>();
  listUnit5$ = new Observable<any[]>();
  unitSearch5;
  debouncer5: Subject<string> = new Subject<string>();
  searchForm: any;
  eventSubscriber: Subscription;
  organizationCategoriesModel: OrganizationCategoriesModel[];
  organizationList: any[];
  SHOW_HIDE_COL_HEIGHT = SHOW_HIDE_COL_HEIGHT;
  listErrorPro: any;
    constructor(
    private activatedRoute: ActivatedRoute,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private translateService: TranslateService,
    private spinner: NgxSpinnerService,
    private toastService: ToastService,
    private organizationCategoriesService: OrganizationCategoriesService,
    private eventManager: JhiEventManager,
    private _http: HttpClient,
    protected router: Router,
    private commonService: CommonService,
    private formStoringService: FormStoringService,
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
    this.columns = this.organizationCategoriesService.columns;
    this.searchForm = {};
    this.startDate = new Date().setDate(1);
    this.endDate = new Date().setDate(1);
    this.buidForm();
    this.onResize();
    this.searchHandle();
    this.registerChange();
    // this.debounceOnSearch();
    this.debounceOnSearch1();
    this.debounceOnSearch2();
    this.debounceOnSearch5();
    this.getDataDropdown();
    this.getDataDropdown2();
  }


  toggleColumns(col) {
    col.isShow = !col.isShow;
    this.organizationCategoriesService.columns = this.columns;
  }

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
    const data = {
      keySearch: term.trim().toUpperCase(),
      type: 'PM'
    }
    this.organizationCategoriesService.getParents(data).subscribe((res => {
      if (this.unitSearch1) {
        const dataRes: any = res;
        this.listUnit1$ = of(dataRes.map((i) => { i.labelName = i.username + '-' + i.email; return i; }).sort((a, b) => a.username.localeCompare(b.username)));
      } else {
        this.listUnit1$ = of([]);
      }
    }));
  }

  onClearUnit1() {
    this.listUnit1$ = of([]);
    this.unitSearch1 = '';
  }

  customSearchFn1(term: string, item: any): any {
    term = term.toLocaleLowerCase().trim();
    return item.username.toLocaleLowerCase().indexOf(term) > -1 ||
      item.email.toLocaleLowerCase().indexOf(term) > -1;
  }

  onSearchUnitClose1() {
    if (!this.form.value.firstName) {
      this.listUnit1$ = of([]);
      this.unitSearch1 = '';
    }
  }

  onSearchUnit2(event) {
    // eslint-disable-next-line no-debugger
    //debugger;
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
    // debugger;
    const data = {
      keySearch: term,
      type: 'STATUSD'
    }

    this.organizationCategoriesService.getPartnerInfo(data).subscribe(res => {
      if (this.unitSearch2) {
        const dataRes: any = res;
        this.listUnit2$ = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));

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
      keySearch: term.trim().toUpperCase(),
      type: 'PARTNER'
    }

    this.organizationCategoriesService.getPartnerInfo(data).subscribe(res => {
      if (this.unitSearch5) {
        const dataRes: any = res;
        this.listUnit5$ = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));
        this.listUnit5$ = of(dataRes.sort((a, b) => a.code.localeCompare(b.code)));
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
  customSearchFn2(term: string, item: any): any {
    term = term.toLocaleLowerCase().trim();
    return item.name.toLocaleLowerCase().indexOf(term) > -1 ||
      item.code.toLocaleLowerCase().indexOf(term) > -1;
  }
  searchHandle() {
    this.page = this.page - 1;
    this.token = localStorage.getItem('token');
    const userToken: any = this.parseJwt(this.token);
    this.humanId = userToken.humanResourceId;
    this.searchForm.humanResourcesId = this.humanId;
    this.organizationCategoriesService.searchProject(this.searchForm).subscribe(result => {
      this.organizationCategoriesModel = result.data;
      this.totalItems = result.dataCount;
    });
  }
  parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
  };
  getDataDropdown() {
    this.listUnit$ = of([]);
    // this.organizationCategoriesService.getParents().subscribe(result => {
    //   this.parentOrganizationList = result.content;
    // });
    const data = {
      keySearch: '',
      type: 'StatusO'
    }
    this.organizationCategoriesService.getStatusOverviewList(data).subscribe(
      result => {
        if (result) {
          const dataRes: any = result;
          this.groupOrganizationList = dataRes;
        } else {
          this.groupOrganizationList = [];
        }
      },
      err => {
        this.groupOrganizationList = [];
      }
    );
  }

  getDataDropdown2() {
    this.listUnit$ = of([]);
    // this.organizationCategoriesService.getParents().subscribe(result => {
    //   this.parentOrganizationList = result.content;
    // });
    const data = {
      keySearch: '',
      type: 'StatusD'
    }
    this.organizationCategoriesService.getStatusOverviewList(data).subscribe(
      result => {
        if (result) {
          const dataRes: any = result;
          this.groupOrganizationList2 = dataRes;
        } else {
          this.groupOrganizationList2 = [];
        }
      },
      err => {
        this.groupOrganizationList2 = [];
      }
    );
  }
  convertDate(str) {
    const date = new Date(str),
      mnth = ('0' + (date.getMonth() + 1)).slice(-2),
      day = ('0' + date.getDate()).slice(-2);
    return [ day,mnth,date.getFullYear()].join('/');
  }

  doSearch() {
     // const r = /^[A-Za-z0-9.]+$/
     // this.searchForm.humanResourcesId = this.humanId;
      this.searchForm.code1 = this.form.value.code !== null ? this.form.value.code.toUpperCase().trim() : this.form.value.code;
      if(this.searchForm.code1 !== null){
        this.searchForm.code = (this.searchForm.code1==="_"||this.searchForm.code1==="%")? "!!!!!!!!!!!!!!!!!!" : this.searchForm.code1;
      }else{
        this.searchForm.code = this.searchForm.code1;
      }
      this.searchForm.partnerID = this.form.value.partnerId;
      this.searchForm.estimateLatchTo = this.form.value.estimateLatchTo;
      this.searchForm.estimateLatchFrom = this.form.value.estimateLatchFrom;
      this.searchForm.pmId = this.form.value.pmId;
      this.searchForm.statusOverview = this.form.value.statusOverview;
      this.searchForm.lstStatusDetail = this.form.value.lstStatusDetail;
      this.searchForm.startDate1 = this.form.value.startDate !== "" ? this.convertDate(this.form.value.startDate) : "";
      this.searchForm.endDate1 = this.form.value.endDate !== "" ? this.convertDate(this.form.value.endDate) : "";
      this.searchForm.page = this.page;
      this.searchForm.pageSize = this.itemsPerPage;

    this.organizationCategoriesService.searchProject(this.searchForm).subscribe(response => {
      this.organizationCategoriesModel = response.data;
      // this.maxSizePage = response.pageCount;
      this.pageSize = response.pageSize;
      this.totalItems = response.dataCount;
    });
  }

  customSearchFn(term: string, item: any) {
    const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    const newRegEx = new RegExp(replacedKey, 'gi');
    const purgedPosition = item.name.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    return newRegEx.test(purgedPosition);
  }

  onChangePosition(event) {
    if (event) {
      this.setValueToField('id', event.id);
      this.setValueToField('name', event.name);
    }
  }

  onClearPosition() {
    this.setValueToField('name', null);
    this.setValueToField('name', null);
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  save() {
    this.router.navigate(['system-categories/add-user']);
  }

  getDate(value) {
    return new Date(value).getTime();
  }

  openEditProject(id) {
    this.router.navigate(['system-categories/organization-categories'], {queryParams: { id}});
  }

  openViewProject(id){
    this.router.navigate(['system-categories/view-user'],{ queryParams: { id: id }});
  }


  openAddProject() {
    this.router.navigate(['system-categories/add-user']);

  }

  openAddEmpToProject(){
      const modalRef = this.modalService.open(AddHumanComponent, {
        size: 'xl',
        centered: true,
        backdrop: 'static',
        keyboard: false,
      });
  }

  openMasterPlan(id){
    this.router.navigate(['system-categories/masterplan'],{ queryParams: { id: id }} );
  }

  checkEstimate() {
    this.message = '';
    if (this.form.value.estimateLatchFrom && this.form.value.estimateLatchTo && this.form.value.estimateLatchFrom > this.form.value.estimateLatchTo) {
      this.message = 'ULNL KH  phê duyệt (MM) từ đang lớn hơn ULNL KH  phê duyệt (MM) đến';
      this.buttonDisable = true;
      return 1;
    } else {
      this.message = '';
      this.buttonDisable = false;
    }
  }

  checkDate() {
    this.message = '';
    const value = this.form.value;
    const endDate = value.endDate;
    const startDate = value.startDate;
    if (endDate !== '' && startDate !== '') {
      if (this.getDate(value.startDate) - this.getDate(value.endDate) > 0) {
        this.message = 'Thời gian bắt đầu đang lớn hơn thời gian kết thúc';
        this.buttonDisable = true;
        return 1;
      } else {
        this.message = '';
        this.buttonDisable = false;
      }
    }
  }

  onDelete(data) {
    if (data.statusOverview !== 14) {
      this.toastService.openErrorToast('Trạng thái dự án khác Chưa khảo sát, không được phép xóa!');
    } else {
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'delete';
      modalRef.componentInstance.param = 'dự án';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.onSubmitDelete(data.projectId);
        }
      });
    }

  }

  onSubmitDelete(id: number) {
    this.organizationCategoriesService.deleteProjects(id).subscribe(result => {
      // this.fetchData();
      this.page = 1;
      this.searchHandle();
      this.toastService.openSuccessToast('Xóa dữ liệu thành công!');
    });
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

  changePageSize(size) {
    this.itemsPerPage = size;
    this.transition();
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    const formValue = this.form.value;
    this.doSearch();
  }

  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('organizationChange', response => this.searchHandle());
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      code: [],
      partnerId:[],
      estimateLatchTo:[],
      estimateLatchFrom:[],
      pmId:[],
      statusOverview:[],
      statusDetail:[],
      startDate:[''],
      endDate:'',
      lstStatusDetail:[],
    });
  }

  openModalAddHuman(type?: string, selectedData?: any) {
    this.router.navigate(['system-categories/add-human']);
    this.commonService.setDataTranfer('projectData',selectedData);

    // if (type === 'addHumanResource') {
    //   const modalRef = this.modalService.open(AddHumanComponent, {
    //     size: 'lg',
    //     backdrop: 'static',
    //     keyboard: false,
    //     windowClass: 'myCustomModalClass'
    //   });
    //   modalRef.componentInstance.type = type;
    //   modalRef.componentInstance.selectedData = selectedData;
    //   modalRef.result
    //     .then(result => {
    //       if (!result) {
    //         this.page = 0;
    //       }
    //     })
    //     .catch(() => {
    //       `1`;
    //     });
    // }
  }

  openModal(id) {
    this.router.navigate(['system-categories/project-management/list-handover-product'],{ queryParams: { id: id }} );
    // if (type === 'listHandoverProduct') {
    //   const modalRef = this.modalService.open(ListHandoverProductComponent, {
    //     size: 'lg',
    //     backdrop: 'static',
    //     keyboard: false,
    //     windowClass: 'myCustomModalClass'
    //   });
    //   modalRef.componentInstance.type = type;
    //   modalRef.componentInstance.selectedData = selectedData;
    //   modalRef.result
    //     .then(result => {
    //       if (!result) {
    //         this.page = 0;
    //       }
    //     })
    //     .catch(() => {
    //       `1`;
    //     });
    // }
  }
  insertUDB(){
    this.organizationCategoriesService.insertDB().subscribe(value =>{

    })
  }
  //nuctv
  synchronized(){
    this.spinner.show();
    this.organizationCategoriesService.synchronized().subscribe(value => {
      this.listErrorPro=value.data;
      this.spinner.hide();
      this.toastService.openSuccessToast("đồng bộ thành công");
      if(this.listErrorPro.length>0){
        const listErr=this.listErrorPro.join("-");
        this.toastService.openWarningToast(listErr,"Các dự án lỗi đồng bộ");
      }
        this.searchHandle();
    }, error => {
      console.warn("thất bại");
      this.spinner.hide();
      this.toastService.openErrorToast("đồng bộ thất bại");
      this.searchHandle();
    }
    )
  }
  advancedSearch(){
    if(this.checkSearch===true){
      this.checkSearch = false;
    }else{
      this.checkSearch = true;
    }

  }

}
