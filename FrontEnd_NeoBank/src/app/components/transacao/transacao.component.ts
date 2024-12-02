import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-transacao',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './transacao.component.html',
  styleUrl: './transacao.component.css'
})
export class TransacaoComponent {

  transactionType: string = ''; // 'transfer' or 'payment'
  amount: number | undefined;
  accountNumber: string = '';
  entity: string = '';
  referenceNumber: string = '';

  // Handle form submission
  onSubmit() {
    // Validate if transaction type and amount are selected
    if (!this.transactionType || !this.amount) {
      alert('Por favor, preencha todos os campos obrigatórios.');
      return;
    }

    // Transaction details
    const transactionDetails = {
      transactionType: this.transactionType,
      amount: this.amount,
      accountNumber: this.transactionType === 'transfer' ? this.accountNumber : null,
      entity: this.transactionType === 'payment' ? this.entity : null,
      referenceNumber: this.transactionType === 'payment' ? this.referenceNumber : null
    };

    console.log('Transaction details:', transactionDetails);

    // Add your logic to send the transaction details to the backend API
  }
}
