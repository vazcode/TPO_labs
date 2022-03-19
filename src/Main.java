import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public final class Main {
    private static final File FILE = new File("file1.dat");
    private static final Random rnd = new Random();

    private static final int Sleep_ = 1000;
    private static final int buff_ = 16;
    private static final int it_count = 10;
    private static final int stop =-1;
    private static final int cont = 1;

    private final RandomAccessFile _file;
    private final MappedByteBuffer _buff;

    public Main(File file, OperationMode mode) throws FExcepts {
        try{
            if (mode == OperationMode.Write)
                file.delete();
            else
            {
                if(!file.exists() && !file.canRead() && !file.isFile())
                    throw new FExcepts(file + "doesnt exists or cant be red");

            }
            _file = new RandomAccessFile(file,"rw");
            FileChannel chan = _file.getChannel();
            _buff = chan.map(FileChannel.MapMode.READ_WRITE, 0, buff_);
        } catch (Throwable e)
        {
            throw new FExcepts(e);
        }
    }

    public void write(){
        write(it_count);
    }

    private static void sleep() {
        try {
            Thread.sleep(Sleep_);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void write(int itcount){
        System.out.println("LOW");
        boolean goInf = (itcount <=0);
        int itLEft = itcount;
        while(goInf || itLEft > 0)
        {
            _buff.rewind();
            _buff.getInt();
            int lastOp = _buff.getInt();
            if(lastOp == OperationMode.Read.getMark())
            {
                System.out.println("LOW2");
                int a1 = rnd.nextInt();
                int a2 = rnd.nextInt();
                _buff.rewind();
                _buff.putInt(cont).putInt(OperationMode.Write.getMark()).putInt(a1).putInt(a2);
                if(!goInf)
                    itLEft--;
            }
            sleep();
        }
        _buff.rewind();
        _buff.putInt(stop).putInt(OperationMode.Write.getMark());
    }

    public void read()
    {
        main:
        while(true){
            _buff.rewind();
            int stopcont = _buff.getInt();
            int lastOp = _buff.getInt();
            if(stopcont == stop && lastOp == OperationMode.Write.getMark()) {
                System.out.println("LOL");
                break main;
            }
            else{
                if(lastOp == OperationMode.Write.getMark()){
                    int a1 = _buff.getInt();
                    int a2 = _buff.getInt();
                    _buff.rewind();
                    _buff.putInt(cont).putInt(OperationMode.Read.getMark());
                    int sum = a1 + a2;
                    System.out.println("sum: " + sum);
                }
            }
            sleep();
        }
    }


    public static void main(String... args) {
        try {
            if (args.length < 1) {
                System.out.println("read/write should be as argument");
                return;
            }
            OperationMode mode = OperationMode.getMode(args[0]);
            if (mode != null) {
                Main file = new Main(FILE, mode);
                switch (mode) {
                    case Write -> file.write();
                    case Read -> file.read();
                    default -> {
                        System.out.println("invalid argument: " + args[0]);
                        return;
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }
}
