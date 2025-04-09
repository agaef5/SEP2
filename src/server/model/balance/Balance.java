package server.model.balance;

public class Balance
{
  private int amount;

  public Balance(int amount){
    this.amount = amount;
  }

  public void setAmount(int amount)
  {
    this.amount = amount;
  }

  public int getAmount(){
    return amount;
  }
}
