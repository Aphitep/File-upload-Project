import { Component, input, Input, signal } from '@angular/core';
import { CofigService, ExcelBody } from '../service/configservice';
import { FileModal } from './file-modal';
import { FileModalForm } from './file-modal-form';
type modeView = 'detail' | 'edit';
@Component({
  selector: 'file-card',
  imports: [FileModal, FileModalForm],
  templateUrl: './file-card.html',
})
export class FileCard {
  constructor(private service: CofigService) {}

  fileData = signal<ExcelBody | null>(null);
  show = signal(false);
  mode = signal<modeView>('detail');
  @Input() filename!: string;

  handleShow(): void {
    this.show.set(!this.show());
  }
  read(mode: modeView) {
    this.mode.set(mode);
    this.service.read(this.filename).subscribe({
      next: (data) => {
        this.fileData.set(data);
        this.handleShow();
        console.log(data);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
