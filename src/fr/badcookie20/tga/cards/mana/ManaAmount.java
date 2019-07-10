package fr.badcookie20.tga.cards.mana;

import fr.badcookie20.tga.utils.Prefixes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ManaAmount {

	private List<ManaNode> manaData;
	
	public ManaAmount(ManaNode... data) {
		manaData = new ArrayList<>();

        Collections.addAll(manaData, data);
	}

    public ManaAmount(List<ManaNode> data) {
        this.manaData = data;
    }

    public int get(ManaType type) {
		if(!hasManaType(type)) return -1;
		
		for(ManaNode node : manaData) {
			if(node.getType() == type) return node.getAmount();
		}
		
		return -1;
	}
	
	public boolean hasManaType(ManaType type) {
		for(ManaNode node : manaData) {
			if(node.getType() == type) return true;
		}
		
		return false;
	}

    public void increment(final ManaAmount amount) {
        // TODO: 20/10/2016 safe the varargs

        final List<ManaNode> finalNodes = new ArrayList<>();
        final List<ManaType> uncalled = new ArrayList<>(Arrays.asList(ManaType.values()));
        final List<ManaNode> nodes = amount.getAllNodes();

        for (ManaNode node : nodes) {
            ManaType type = node.getType();
            if(this.hasManaType(node.getType())) {
                finalNodes.add(new ManaNode(type, node.getAmount() + this.get(type)));
            }else{
                finalNodes.add(new ManaNode(type, node.getAmount()));
            }

            uncalled.remove(type);
        }

        if(!uncalled.isEmpty()) {
            for(ManaType uncalledType : uncalled) {
                if(this.hasManaType(uncalledType)) {
                    finalNodes.add(new ManaNode(uncalledType, this.get(uncalledType)));
                }
            }
        }

        this.manaData = finalNodes;
    }

    public void decrement(ManaAmount manaAmount) {
	    this.increment(manaAmount.turnNegative());
    }

	public boolean hasOnlyUndefined() {
        return manaData.size() == 1 && manaData.get(0).getType() == ManaType.UNDEFINED;
    }
	
	@Override
	public String toString() {
		String s = "";
        int i = this.manaData.size();
		
		for(ManaNode node : manaData) {
            i--;
			s+= Prefixes.MANA_TYPE;
            s+=node.getAmount();
			s+=node.getType();
			if(i != 0) s+=" + ";
		}
		
		return s;
	}

    public List<ManaNode> getAllNodes() {
        return manaData;
    }

    public boolean isEnough(ManaAmount manaCost) {
        for(ManaNode node : manaCost.getAllNodes()) {
            if(!this.hasManaType(node.getType())) {
                return false;
            }

            if(node.getAmount() > this.get(node.getType())) {
                return false;
            }
        }

        return true;
    }

    public ManaAmount turnNegative(){
	    List<ManaNode> newNodes = new ArrayList<>();

	    for(ManaNode node : manaData) {
	        newNodes.add(new ManaNode(node.getType(), -node.getAmount()));
        }

        this.manaData = newNodes;

	    return this;
    }
}
