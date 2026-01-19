import { Component, signal } from '@angular/core';
import { CofigService } from './service/configservice';
import { FileCard } from './components/file-card';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-root',
  imports: [FileCard, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  constructor(private service: CofigService) {}

  file: File | null = null;
  fileLists: string[] = [];
  statusMessage = signal<string>('');

  handleMessage = (mss: string) => {
    this.statusMessage.set(mss);

    setTimeout(() => {
      this.statusMessage.set('');
    }, 3000);
  };

  handleFileChange(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.file = input.files[0];
    }
  }

  onUpload() {
    if (!this.file) return;
    this.service.upload(this.file).subscribe({
      next: (res) => {
        if (res) {
          this.handleMessage('Upload Successfuly');
        }
      },
      error: (err) => {
        this.handleMessage('Only .xlsx or .xls are allowed');
      },
    });
  }

  ngOnInit() {
    this.service.list().subscribe({
      next: (data) => {
        this.fileLists = data;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
