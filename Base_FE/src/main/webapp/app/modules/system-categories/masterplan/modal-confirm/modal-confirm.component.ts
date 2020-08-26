import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ConfirmModalComponent} from 'app/shared/components/confirm-modal/confirm-modal.component';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {HeightService} from 'app/shared/services/height.service';
import {ToastService} from "app/shared/services/toast.service";
import {MasterplanServiceService} from 'app/core/services/masterplan/masterplan-service.service';

@Component({
  selector: 'jhi-modal-confirm',
  templateUrl: './modal-confirm.component.html',
  styleUrls: ['./modal-confirm.component.scss']
})
export class ModalConfirmComponent implements OnInit {
  @Input() model;
  @Input() type;
  @Output() recorded = new EventEmitter<any>();
  // @Output() recordednote = new EventEmitter<string>();;
  // @Output() status = new EventEmitter<number>();;
  isModalConfirmShow = false;
  height: number;
  reason: string;
  note: string;

  constructor(
    private modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private heightService: HeightService,
    private toastService: ToastService,
    private masterplanServiceService : MasterplanServiceService,
  ) {

  }


  ngOnInit() {
    this.onResize();
  }


  onCloseAddModal() {
    this.isModalConfirmShow = true;
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'confirm';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.activeModal.dismiss();
      }
      this.isModalConfirmShow = false;
    });
  }

  confirm(reason: string, note: string, c: number) {
    if((reason ===  null || reason==='' || reason === undefined) && c === 2){
      this.toastService.openWarningToast("Cần điền lý do từ chối");
    } else {
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'confirmModal';
      if(c===1){
      modalRef.componentInstance.message = 'Bạn có muốn xác nhận kế hoạch không?';}
      else{modalRef.componentInstance.message = 'Bạn có muốn từ chối kế hoạch không?';}
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.activeModal.dismiss();
          this.recorded.emit({reason: reason, note: note, status: c});
          if (this.type === 1) {
            this.model.noteBA = note;
            this.model.reasonBA = reason;
            this.model.statusBA = c;

          }
          if (this.type === 2) {
            this.model.noteTest = note;
            this.model.reasonTest = reason;
            this.model.statusTest = c;
          }
          if (this.type === 3) {
            this.model.notePM = note;
            this.model.reasonPM = reason;
            this.model.statusPM = c;
          }
          if (this.type === 4) {
            this.model.noteQA = note;
            this.model.reasonQA = reason;
            this.model.statusQA = c;
          }
          this.masterplanServiceService.confirm(this.model).subscribe(data => {

          })
        }
      })

    }
  }


  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

}
