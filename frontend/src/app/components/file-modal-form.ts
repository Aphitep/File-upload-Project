import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CofigService, ExcelBody } from '../service/configservice';

@Component({
  selector: 'file-modal-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './file-modal-form.html',
})
export class FileModalForm {
  constructor(private service: CofigService) {}
  @Input() filedata!: ExcelBody | null;
  @Output() cancelForm = new EventEmitter<void>();

  objectKeys = Object.keys;
  onCancel() {
    this.cancelForm.emit();
  }

  onSave() {
    this.service.write(this.filedata!).subscribe({
      next: (res) => {
        this.cancelForm.emit();
      },
      error: (err) => console.error(err),
    });
  }
}
