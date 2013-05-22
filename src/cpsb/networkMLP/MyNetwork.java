package cpsb.networkMLP;

import java.util.Arrays;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import commConnector.CommListenerInterface;

public class MyNetwork implements CommListenerInterface{
	
	private BasicNetwork network;
	private ResilientPropagation train;
	private MLDataSet trainingSet;
	private MLDataSet testSet;
	private double [][] INPUT;
	private double [][] IDEAL;
	MyNetwork(int hiddenNeurons){
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 128));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hiddenNeurons));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		network.getStructure().finalizeStructure();
		network.reset();
	}
	
	public void trainNetwork(){
		trainingSet = new BasicMLDataSet(INPUT, IDEAL);
		train = new ResilientPropagation(network, trainingSet);
		do {
			train.iteration();
		} while(train.getError() > 0.01);
	}
	
	public void gainTrainingData(boolean isEyesClosed){
		
	}
	
	public void testNetwork(){
		for(MLDataPair pair: trainingSet ) {
			final MLData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
					+ ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
		}
	}
	
	private byte[] data = new byte[MAX_LENGTH];
	private int counter = 0;
	private static final int MAX_LENGTH = 128;
	@Override
	public void messageReceived(byte[] message) {
		int len = message.length;
		if(len+counter>MAX_LENGTH){
			int difference = len+counter-MAX_LENGTH;
			for(int i=0; i<difference;i++)
				data[counter++]=message[i];
			/*
			 * place to send data to network
			 */
			counter=0;
			byte[] remainMessage = Arrays.copyOfRange(message, difference, len);
			messageReceived(remainMessage);
		}
		else{
			for(int i=0; i<len; i++)
				data[counter++]=message[i];
		}
		if(counter==128){
			
		}
	}
	@Override
	public void portAlreadyInUse() {}
	@Override
	public void writingError() {}
	@Override
	public void readingError() {}
}
