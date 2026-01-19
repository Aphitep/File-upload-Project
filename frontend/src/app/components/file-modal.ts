import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CofigService, ExcelBody } from '../service/configservice';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { blob } from 'stream/consumers';

@Component({
  selector: 'file-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './file-modal.html',
})
export class FileModal {
  constructor(private service: CofigService) {}
  @Input() filedata!: ExcelBody | null;
  @Output() cancelDetail = new EventEmitter<void>();

  objectKeys = Object.keys;
  onCancel() {
    this.cancelDetail.emit();
  }

  download() {
    if (!this.filedata?.filename) return;
    this.service.download(this.filedata.filename).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = this.filedata!.filename;

        a.click();

        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
